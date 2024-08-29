package lan.pass.demo.service;

import cn.hutool.core.codec.Base64;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import de.brendamour.jpasskit.*;
import de.brendamour.jpasskit.passes.PKGenericPass;
import de.brendamour.jpasskit.passes.PKGenericPassBuilder;
import de.brendamour.jpasskit.personalization.PKPersonalization;
import de.brendamour.jpasskit.signing.*;
import lan.pass.demo.mapper.PassMapper;
import lan.pass.demo.model.*;
import lan.pass.demo.model.ChangeMessageType;
import lan.pass.demo.model.ChangeMessage;
import lan.pass.demo.request.PassRequest;
import lan.pass.demo.request.Personalization;
import lan.pass.demo.utility.Compressor;
import lan.pass.demo.utility.DirectoryHashWriter;
import lan.pass.demo.utility.PassConverter;
import lombok.var;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
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

    @Resource
    private PassMapper passMapper;

    @Resource
    private HistoryService historyService;

    @Resource
    private AssetService assetService;

    @Resource
    private ChangeMessageService changeMessageService;

    /**
     * 创建一个 Pass 对象，并使用签名信息生成签名的 .pkpass 文件。
     * @return 创建的 Pass 对象。
     */
    public PKPass createPKPass(PassRequest passRequest, CreatePKPassType createPKPassType){
        // Check for required fields and return null if they are not present
        if (createPKPassType == CreatePKPassType.Create) {
            if (passRequest.getOwnerId() == null || passRequest.getPassTypeIdentifier() == null || passRequest.getSerialNumber() == null) {
                return null;
            }
        }
        if (createPKPassType != CreatePKPassType.Read){
            changeMessageService.deletePassChangeMessageByPassId(passRequest);
            for (ChangeMessageType type : ChangeMessageType.fieldHashMap.keySet()){
                Field field = ChangeMessageType.fieldHashMap.get(type);
                try {
                    field.setAccessible(true);
                    Object convertedResult = field.get(passRequest);
                    if (convertedResult != null){
                        ArrayList<ChangeMessage> items = new ArrayList<>();
                        if (convertedResult instanceof ChangeMessage){
                            items.add((ChangeMessage)convertedResult);
                        } else if (convertedResult instanceof List) {
                            items = (ArrayList<ChangeMessage>)convertedResult;
                        }
                        changeMessageService.insertRelationship(passRequest, items, type);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            changeMessageService.loadChangeMessageItem(passRequest);
        }
        PKGenericPassBuilder pkGenericPassBuilder = PKGenericPass.builder()
                .primaryFields(changeMessageService.siftThrough(passRequest.getDataPrimaryFields(), PKField.class, createPKPassType == CreatePKPassType.Read))
                .secondaryFields(changeMessageService.siftThrough(passRequest.getDataSecondaryFields(), PKField.class, createPKPassType == CreatePKPassType.Read))
                .auxiliaryFields(changeMessageService.siftThrough(passRequest.getDataAuxiliaryFields(), PKField.class, createPKPassType == CreatePKPassType.Read))
                .backFields(changeMessageService.siftThrough(passRequest.getDataBackFields(), PKField.class, createPKPassType == CreatePKPassType.Read))
                .headerFields(changeMessageService.siftThrough(passRequest.getDataHeaderFields(), PKField.class, createPKPassType == CreatePKPassType.Read));
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
        if (passRequest.getDataBeacons() != null) {
            builder.beacons(changeMessageService.siftThrough(passRequest.getDataBeacons(), PKBeacon.class, createPKPassType == CreatePKPassType.Read));
        }
        if (passRequest.getDataLocations() != null) {
            builder.locations(changeMessageService.siftThrough(passRequest.getDataLocations(), PKLocation.class, createPKPassType == CreatePKPassType.Read));
        }
        if (passRequest.getBarcodes() != null) {
            builder.barcodes(passRequest.getBarcodes());
        }
        if (passRequest.getAssociatedStoreIdentifiers() != null) {
            builder.associatedStoreIdentifiers(passRequest.getAssociatedStoreIdentifiers());
        }
        if (passRequest.getAppLaunchURL() != null) {
            builder.appLaunchURL(passRequest.getAppLaunchURL());
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
        if (passRequest.getDataRelevantDate() != null) {
            List<ChangeMessage> cmt = new ArrayList<>();
            cmt.add(passRequest.getDataRelevantDate());
            var result = changeMessageService.siftThrough(cmt, String.class, createPKPassType == CreatePKPassType.Read);
            if (result.size() > 0){
                ZonedDateTime zonedDateTime = ZonedDateTime.parse(result.get(0), DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                builder.relevantDate(zonedDateTime.toInstant());
            }
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
        return builder.build();
    }

    private byte[] makePassFile(PKPass pass, Long id, PassRequest passRequest, MakePassType makePassType){
        //创建签名
        PKSigningInformation pkSigningInformation = null;
        try {
            pkSigningInformation = new PKSigningInformationUtil().loadSigningInformationFromPKCS12AndIntermediateCertificate(KEY_STORE_PATH, PRIVATE_KEY_PASSWORD, APPLE_WWDRCA);
        } catch (IOException | CertificateException e) {
            e.printStackTrace();
        }
        String folderPath = String.format("pass/%d", id);
        File folder = new File(folderPath);
        // 如果要创建文件夹，则删除文件夹原有内容
        if (makePassType == MakePassType.Directory){
            // 使用FileUtils删除文件夹及其内容
            if (folder.exists()) {
                try {
                    FileUtils.deleteDirectory(folder);
                } catch (IOException e) {
                    e.printStackTrace();
                    System.err.println("Failed to delete existing folder: " + folderPath);
                    return null;
                }
            }
        }
        IPKPassTemplate template = null;
        //对于文件系统内
        if (makePassType != MakePassType.Memory){
            // 检查并删除已存在的pass/{id}.pkpass文件
            String pkPassFilePath = String.format("pass/%d.pkpass", id);
            File pkPassFile = new File(pkPassFilePath);
            if (pkPassFile.exists()) {
                if (!pkPassFile.delete()) {
                    System.err.println("Failed to delete existing pass file: " + pkPassFilePath);
                    return null;
                }
            }
            // 确保文件夹存在
            if (!folder.exists()) {
                boolean folderCreated = folder.mkdirs();
                if (!folderCreated) {
                    System.err.println("Failed to create folder: " + folderPath);
                    return null;
                }
            }

            //添加静态图片
            if (passRequest.getIcon() != null){
                ImageService.setImageFile(id, passRequest.getIcon(), "icon");
            }
            if (passRequest.getLogo() != null){
                ImageService.setImageFile(id, passRequest.getLogo(), "logo");
            }
            if (passRequest.getStrip() != null){
                ImageService.setImageFile(id, passRequest.getStrip(), "strip");
            }
            template = new PKPassTemplateFolder(String.format("pass/%d", id));
        }
        //内存
        else {
            PKPassTemplateInMemory memory = new PKPassTemplateInMemory();
            try {
                memory.addFile(PKPassTemplateInMemory.PK_STRIP, passRequest.getStrip());
                memory.addFile(PKPassTemplateInMemory.PK_ICON, passRequest.getIcon());
                memory.addFile(PKPassTemplateInMemory.PK_LOGO, passRequest.getLogo());
            } catch (IOException e) {
                e.printStackTrace();
            }
            template = memory;
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
        PKFileBasedSigningUtil pkSigningUtil = new PKFileBasedSigningUtil();
        try {
            byte[] signedAndZippedPkPassArchive = pkSigningUtil.createSignedAndZippedPersonalizedPkPassArchive(pass, pkPersonalization, template, pkSigningInformation);
            if (makePassType != MakePassType.Memory){
                String filePath = String.format("pass/%d.pkpass", id);
                try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
                    fileOutputStream.write(signedAndZippedPkPassArchive);
                    System.out.println("Pass file has been written successfully.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (makePassType == MakePassType.Directory){
                    String extractPath = String.format("pass/%d", id);
                    Compressor.unzipFile(filePath, extractPath);
                    File zipFile = new File(filePath);
                    if (!zipFile.delete()) {
                        System.err.println("Failed to delete zip file: " + filePath);
                    }
                }
            } else {
                return signedAndZippedPkPassArchive;
            }
        } catch (PKSigningException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Object createPass(PassRequest passRequest){
        //数据库创建，得到id
        Pass dataPass = new Pass();
        dataPass.setOwnerId(passRequest.getOwnerId());
        dataPass.setName(passRequest.getName());
        dataPass.setIsTemplate(passRequest.getIsTemplate() != null? passRequest.getIsTemplate() : false);
        //parent table
        Asset asset = assetService.createAsset(passRequest.getName(), passRequest.getOwnerId(), AssetType.Pass);
        dataPass.setAssetId(asset.getId());
        try {
            passMapper.insertPass(dataPass);
        } catch (DuplicateKeyException e) {
            return "存在重复名称的" + (dataPass.getIsTemplate()? "模板" : "pass");
        }
        passRequest.setId(dataPass.getId());
        PKPass pass = createPKPass(passRequest, CreatePKPassType.Create);
        if (pass == null){
            return "error, please check if there's missing message.";
        }
        Long id = dataPass.getId();
        makePassFile(pass, id, passRequest, MakePassType.Directory);
        //历史记录
        historyService.createHistory(new History(3L, passRequest.getOwnerId(), LocalDateTime.now(), id));
        return pass;
    }

    public byte [] getPassFileContent(Long id){
        if (StringUtils.isNotBlank(id.toString())) {
            PassRequest request = getPKPassRequestById(id);
            PKPass pass = createPKPass(request, CreatePKPassType.Read);
            return makePassFile(pass, id, request, MakePassType.Memory);
        }
        return null;
    }
    public HashMap<String, JsonNode> initializeFileJSON(Long id) {
        HashMap<String, JsonNode> filesMap = new HashMap<>();
        ObjectMapper objectMapper = new ObjectMapper();
        String directoryPath = "pass/" + id; // Replace with your actual directory path

        File directory = new File(directoryPath);
        if (directory.isDirectory()) {
            File[] files = directory.listFiles((dir, name) -> name.endsWith(".json") || name.endsWith(".png"));
            if (files != null) {
                for (File file : files) {
                    String fileName = file.getName();
                    try {
                        byte[] fileContentBytes = org.apache.commons.io.FileUtils.readFileToByteArray(file);
                        String fileContent = new String(fileContentBytes, StandardCharsets.UTF_8);

                        JsonNode fileData = null;
                        if (fileName.endsWith(".json")) {
                            fileData = objectMapper.readTree(fileContent);
                        } else if (fileName.endsWith(".png")) {
                            // 将PNG文件内容转换为Base64字符串
                            String base64Content = Base64.encode(fileContentBytes);
                            fileData = objectMapper.valueToTree(base64Content);
                        }
                        filesMap.put(fileName, fileData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return filesMap;
    }

    public HashMap<String, JsonNode> getEditContent(Long id) {
        HashMap<String, JsonNode> filesMap = initializeFileJSON(id);
        PassRequest request = getPKPassRequestById(id);
        changeMessageService.loadChangeMessageItem(request);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String requestJson = objectMapper.writeValueAsString(request);
            JsonNode requestNode = objectMapper.readTree(requestJson);
            filesMap.put("pass.json", requestNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filesMap;
    }
    private PassRequest getPKPassRequestById(Long id) {
        HashMap<String, JsonNode> map = initializeFileJSON(id);
        PassRequest request = null;
        try {
            request = PassConverter.populatePassRequest(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        request.setId(id);
        return request;
    }

    public Object editPass(PassRequest passRequest, Long specifiedId){
        Long id = specifiedId == null? passRequest.getId() : specifiedId;
        PassRequest originalRequest = new PassRequest();
        if (getPKPassRequestById(id) != null){
            originalRequest = getPKPassRequestById(id);
        }
        originalRequest.coverBy(passRequest);
        originalRequest.setId(id);
        PKPass pass = createPKPass(originalRequest, CreatePKPassType.Edit);
        if (pass == null){
            return "error, please check if there's missing message.";
        }
        Pass passData = new Pass();
        passData.setName(passRequest.getName());
        passMapper.updatePass(passData);
        makePassFile(pass, id, originalRequest, MakePassType.Directory);
        //历史记录
        historyService.createHistory(new History(2L, passRequest.getOwnerId(), LocalDateTime.now(), id));
        return pass;
    }

    public Object editMultiplePasses(PassRequest passRequest){
        for (Long id : passRequest.getIds()){
            editPass(passRequest, id);
        }
        return "success";
    }

    public Pass getPassById(Long id) {
        return passMapper.selectPassById(id);
    }

    public List<Pass> getAllPasses() {
        return passMapper.selectAllPasses();
    }

    public List<Pass> selectPassByOwnerId(Long id, boolean isTemplate) {
        return passMapper.selectPassByOwnerId(id, isTemplate);
    }

    public List<Pass> selectPassByMerchantId(Long id, boolean isTemplate) {
        return passMapper.selectPassByMerchantId(id, isTemplate);
    }

    public Pass updatePass(Long id, Pass passDetails) {
        passDetails.setId(id);
        passMapper.updatePass(passDetails);
        return passDetails;
    }

    public int deletePass(Long id, Long ownerId) {
        int affectedRows = passMapper.deletePass(id, ownerId);
        //历史记录
        historyService.createHistory(new History(4L, ownerId, LocalDateTime.now(), id));
//        String directoryPath = String.format("pass/%d", id);
//        File directoryToDelete = new File(directoryPath);
//        FileDeletor.deleteDirectory(directoryToDelete);
        return affectedRows;
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
enum MakePassType{
    File,
    Directory,
    Memory;
}
enum CreatePKPassType{
    Create, //创建pass文件
    Read, //读取pass文件
    Edit;
}