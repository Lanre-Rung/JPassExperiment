package lan.pass.demo.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.brendamour.jpasskit.*;
import de.brendamour.jpasskit.passes.PKGenericPass;
import de.brendamour.jpasskit.passes.PKGenericPassBuilder;
import de.brendamour.jpasskit.personalization.PKPersonalization;
import de.brendamour.jpasskit.personalization.PKPersonalizationBuilder;
import de.brendamour.jpasskit.signing.*;
import lan.pass.demo.mapper.PassMapper;
import lan.pass.demo.model.Pass;
import lan.pass.demo.request.PassRequest;
import lan.pass.demo.request.Personalization;
import lan.pass.demo.utility.Compressor;
import lan.pass.demo.utility.DirectoryHashWriter;
import lan.pass.demo.utility.FileDeletor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.io.*;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 服务类，用于创建和管理 Pass 功能。
 * 提供创建 Pass 并将其写入 JSON 文件，以及生成签名的 .pkpass 文件的方法。
 */
@Service
public class PassService {
    /**
     * PKCS#12 证书文件的存储路径。
     */
    public static final String KEY_STORE_PATH = "reference/pass.com.newland.passtest3.p12";
    /**
     * 私钥的密码。
     */
    public static final String PRIVATE_KEY_PASSWORD = "123456";
    /**
     * Apple 根证书的路径。
     */
    public static final String APPLE_WWDRCA = "reference/AppleWWDRMPCA1G1.cer";
    /**
     * 模板文件的路径。
     */
    public static final String template_path = "reference/Attachment-apple";

    private PassMapper passMapper;

    @Autowired
    public void setPassMapper(PassMapper passMapper) {
        this.passMapper = passMapper;
    }

    /**
     * 创建一个 Pass 对象，并使用签名信息生成签名的 .pkpass 文件。
     * @return 创建的 Pass 对象。
     */
    public PKPass createPass(PassRequest passRequest){
        // Check for required fields and return null if they are not present
        if (passRequest.getPassTypeIdentifier() == null || passRequest.getSerialNumber() == null) {
            return null;
        }
        PKGenericPassBuilder pkGenericPassBuilder = PKGenericPass.builder()
                .primaryFields(passRequest.getPrimaryFields())
                .secondaryFields(passRequest.getSecondaryFields())
                .auxiliaryFields(passRequest.getAuxiliaryFields())
                .backFields(passRequest.getBackFields())
                .headerFields(passRequest.getHeaderFields());
        if (passRequest.getTransitType() != null){
            pkGenericPassBuilder.transitType(passRequest.getTransitType());
        }
        if (passRequest.getPassType() != null){
            pkGenericPassBuilder.passType(passRequest.getPassType());
        }
        PKPassBuilder builder = PKPass.builder().pass(pkGenericPassBuilder);
        builder.passTypeIdentifier(passRequest.getPassTypeIdentifier())
            .serialNumber(passRequest.getSerialNumber());
        //Check if other data are present and add them if necessary
        if (passRequest.getFormatVersion() != null) {
            builder.formatVersion(passRequest.getFormatVersion());
        }
        if (passRequest.getTeamIdentifier() != null) {
            builder.teamIdentifier(passRequest.getTeamIdentifier());
        }
        if (!StringUtils.isEmpty(passRequest.getOrganizationName())) {
            builder.organizationName(passRequest.getOrganizationName());
        }
        if (passRequest.getLogoText() != null) {
            builder.logoText(passRequest.getLogoText());
        }
        if (passRequest.getDescription() != null) {
            builder.description(passRequest.getDescription());
        }
        if (passRequest.getBackgroundColor() != null) {
            builder.backgroundColor(passRequest.getBackgroundColor());
        }
        if (passRequest.getForegroundColor() != null) {
            builder.foregroundColor(passRequest.getForegroundColor());
        }
        if (passRequest.getLabelColor() != null) {
            builder.labelColor(passRequest.getLabelColor());
        }
        if (passRequest.getWebServiceURL() != null) {
            builder.webServiceURL(passRequest.getWebServiceURL());
        }
        if (passRequest.getAuthenticationToken() != null) {
            builder.authenticationToken(passRequest.getAuthenticationToken());
        }
        if (passRequest.getNfc() != null) {
            builder.nfc(passRequest.getNfc());
        }
        if (passRequest.getBeacons() != null) {
            builder.beacons(passRequest.getBeacons());
        }
        if (passRequest.getLocations() != null) {
            builder.locations(passRequest.getLocations());
        }
        if (passRequest.getBarcodes() != null) {
            builder.barcodes(passRequest.getBarcodes());
        }
        if (passRequest.getAssociatedStoreIdentifiers() != null) {
            builder.associatedStoreIdentifiers(passRequest.getAssociatedStoreIdentifiers());
        }
        if (passRequest.getAssociatedApps() != null) {
            builder.associatedApps(passRequest.getAssociatedApps());
        }
        if (passRequest.getUserInfo() != null) {
            builder.userInfo(passRequest.getUserInfo());
        }
        if (passRequest.getMaxDistance() != null) {
            builder.maxDistance(passRequest.getMaxDistance());
        }
        if (passRequest.getRelevantDate() != null) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(passRequest.getRelevantDate(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            builder.relevantDate(zonedDateTime.toInstant());
        }
        if (passRequest.getExpirationDate() != null) {
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(passRequest.getExpirationDate(), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
            builder.expirationDate(zonedDateTime.toInstant());
        }
        if (passRequest.getVoided() != null) {
            builder.voided(passRequest.getVoided());
        }
        if (passRequest.getSharingProhibited() != null) {
            builder.sharingProhibited(passRequest.getSharingProhibited());
        }
        if (passRequest.getSemantics() != null) {
            builder.semantics(passRequest.getSemantics());
        }
        PKPass pass = builder.build();
        makePassFile(pass, passRequest);
        return pass;
    }

    private void makePassFile(PKPass pass, PassRequest passRequest){
        PKSigningInformation pkSigningInformation = null;
        try {
            pkSigningInformation = new PKSigningInformationUtil().loadSigningInformationFromPKCS12AndIntermediateCertificate(KEY_STORE_PATH, PRIVATE_KEY_PASSWORD, APPLE_WWDRCA);
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }
        //数据库创建，得到id
        Pass dataPass = new Pass();
        dataPass.setOwnerId(passRequest.getOwnerId());
        dataPass.setName(passRequest.getName());
        passMapper.insertPass(dataPass);
        Long id = dataPass.getId();
        // 创建文件夹路径
        String folderPath = String.format("pass/%d", id);
        File folder = new File(folderPath);

        // 确保文件夹存在
        if (!folder.exists()) {
            boolean folderCreated = folder.mkdirs();
            if (!folderCreated) {
                System.err.println("Failed to create folder: " + folderPath);
                return;
            }
        }
        //添加静态图片
        if (passRequest.getIcon() != null){
            ImageService.setIcon(id, passRequest.getIcon());
        }
        if (passRequest.getLogo() != null){
            ImageService.setLogo(id, passRequest.getLogo());
        }
        if (passRequest.getStrip() != null){
            ImageService.setStrip(id, passRequest.getStrip());
        }
        //添加个性化内容
        Personalization personalization = passRequest.getPersonalization();
        PKPersonalization pkPersonalization = null;
        if (personalization != null){
            pkPersonalization = PKPersonalization.builder()
                .requiredPersonalizationFields(personalization.getRequiredPersonalizationFields())
                .description(personalization.getDescription())
                .termsAndConditions(personalization.getTermsAndConditions()).build();
        }

        PKPassTemplateFolder passTemplate = new PKPassTemplateFolder(String.format("pass/%d", id));
        PKFileBasedSigningUtil pkSigningUtil = new PKFileBasedSigningUtil();
        try {
            byte[] signedAndZippedPkPassArchive = pkSigningUtil.createSignedAndZippedPersonalizedPkPassArchive(pass, pkPersonalization, passTemplate, pkSigningInformation);
            String filePath = String.format("pass/%d.pkpass", id);
            try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                fileOutputStream.write(signedAndZippedPkPassArchive);
                System.out.println("Pass file has been written successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //TODO: 解压到，删除压缩包
            String extractPath = String.format("pass/%d", id);
            Compressor.unzipFile(filePath, extractPath);
            /*
            File zipFile = new File(filePath);
            if (!zipFile.delete()) {
                System.err.println("Failed to delete zip file: " + filePath);
            }
            *
             */
        } catch (PKSigningException e) {
            e.printStackTrace();
        }
    }

    public Pass getPassById(Long id) {
        return passMapper.selectPassById(id);
    }

    public List<Pass> getAllPasses() {
        return passMapper.selectAllPasses();
    }

    public Pass updatePass(Long id, Pass passDetails) {
        passDetails.setId(id);
        passMapper.updatePass(passDetails);
        return passDetails;
    }

    public void deletePass(Long id) {
        passMapper.deletePass(id);
        String directoryPath = String.format("pass/%d", id);
        File directoryToDelete = new File(directoryPath);
        FileDeletor.deleteDirectory(directoryToDelete);
    }
    /**
     * 创建 Pass 并将其转换为 JSON 格式写入文件。
     * @param pass 要写入的 Pass 对象。
     */
    @Deprecated
    public void createPassAndWriteToJson(PKPass pass) {
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(pass);

            Writer writer = new FileWriter("temp/pass.json");
            writer.write(json);
            writer.close();
            DirectoryHashWriter.calculateHashesForDirectoryAndWriteToFile("temp", "temp/manifest.json");
            Compressor.zipDirectory("temp", "pass.pkpass");
            System.out.println("Pass written to pass.json");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
