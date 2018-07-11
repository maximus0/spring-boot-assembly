package com.xinhuanet.censor.service.impl;

import cn.news.dubbo.AlbumServiceFacade;
import cn.news.dubbo.ImageServiceFacade;
import cn.news.photo.domain.Album;
import cn.news.photo.vo.ImageVO;
import com.alibaba.fastjson.JSON;
import com.trs.blog.domain.Article;
import com.trs.blog.logic.ArticleManager;
import com.trs.blog.logic.ArticleSysCategoryManager;
import com.xinhuanet.censor.service.ArticleFileService;
import com.xinhuanet.censor.util.ArticleHelper;
import com.xinhuanet.censor.util.Lang;
import com.xinhuanet.video.domain.VideoArticle;
import com.xinhuanet.video.domain.VideoCategory;
import com.xinhuanet.video.intf.VideoArticleService;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by conanca on 15-6-3.
 */
@Service
public class ArticleServiceImpl implements ArticleFileService {
    private static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    /**
     * 生成xml后存放到的本地目录
     */
    @Value("${article.path}")
    private String localPath;

    @Value("${article.imgDirPath}")
    private String imgDirPath;

    @Value("${article.defaultImg}")
    private String defaultImg;

    @Autowired
    private ArticleManager articleManager;
    @Autowired
    private ArticleSysCategoryManager articleSysCategoryManager;
    @Autowired
    private ImageServiceFacade imageService;
    @Autowired
    private  AlbumServiceFacade albuService;
    @Autowired
    private VideoArticleService videoArticleService;

    @Override
    public Map genXuanCmsMessage(Map message) {
        Integer appId = (Integer) message.get("appId");
        String column = (String) message.get("column");
        String contentId = (String) message.get("contentId");


        Map xuan = new HashMap();
        String origin = "";
        if (appId == null) {
            logger.error("参数异常，appId==null");
            return xuan;
        }

        xuan.put("appId", appId);
        xuan.put("column", column);
        xuan.put("contentId", contentId);


        if (appId == 20) {
            origin = "新华博客";
            xuan.put("origin", origin);
            gen20Message(xuan, contentId);
        } else if (appId == 30) {
            origin = "新华拍客";
            xuan.put("origin", origin);
            if (column.equals("1")){
                gen301Message(xuan, contentId);
            }else if (column.equals("2")){
                gen302Message(xuan, contentId);
            }

        } else if (appId == 40) {
            origin = "新华播客";
            xuan.put("origin", origin);
            gen40Message(xuan, contentId);
        }


        return xuan;
    }

    /**
     * 生成博客推送炫知消息
     *
     * @param xuan
     * @param contentId
     */
    public void gen20Message(Map xuan, String contentId) {



        // 获取 article对象
        Article article = articleManager.getArticle(Integer.valueOf(contentId));

        if(Lang.isEmpty(article)){
            logger.error("查询不到博客 Article 对象 contentId："+contentId);
            xuan.clear();
            return;
        }
        String title = article.getTitle();
        String contentTxt = article.getContentHtml();
        String author = article.getNickName();
        String originUrl = ArticleHelper.makePermanentURL(article);
        String tag = article.getTagWords();
        int categoryId = article.getSystemCategoryId();
        if (!StringUtils.isEmpty(tag)) {
            tag = tag.replaceAll(" ", ",");
        }
        String description = article.getBrief();

        xuan.put("contentTxt", contentTxt);
        xuan.put("title", title);
        xuan.put("description", description);
        xuan.put("author", author);
        xuan.put("originUrl", originUrl);
        xuan.put("tag", tag);
        xuan.put("categoryId", categoryId);
    }

    /**
     * 生成拍客相册推送炫知消息
     *
     * @param xuan
     * @param contentId
     */
    public void gen301Message(Map xuan, String contentId) {

        // 获取 Album

        Album article =  albuService.get(Integer.valueOf(contentId));
        if(Lang.isEmpty(article)){
            logger.error("查询不到拍客 Album 对象 contentId："+contentId);
            xuan.clear();
            return;
        }
        String title = article.getTitle();
        String author = article.getNickName();
        String originUrl =  ArticleHelper.makeAlbumURL(article);
        String tag = article.getTagWords();
        int categoryId = article.getSysCatId();
        if (!StringUtils.isEmpty(tag)) {
            tag = tag.replaceAll(" ", ",");
        }
        String description = article.getBrief();


        xuan.put("title", title);
        xuan.put("description", description);
        xuan.put("author", author);
        xuan.put("originUrl", originUrl);
        xuan.put("tag", tag);
        xuan.put("categoryId", categoryId);
        xuan.put("picDesc", description);
    }

    /**
     * 生成拍客相片推送炫知消息
     *
     * @param xuan
     * @param contentId
     */
    public void gen302Message(Map xuan, String contentId) {

    // 获取 ImageVO
        ImageVO image = imageService.get(Integer.valueOf(contentId));
        if(Lang.isEmpty(image)){
            logger.error("查询不到拍客 ImageVO 对象 contentId："+contentId);
            xuan.clear();
            return;
        }
        Integer albumId = image.getAlbumId();
        String title = image.getTitle();
        String author = image.getNickName();
        String originUrl = ArticleHelper.makePhotoURL(image);
        String tag = image.getTagWords();
        int categoryId = image.getSysCatId();
        if (!StringUtils.isEmpty(tag)) {
            tag = tag.replaceAll(" ", ",");
        }
        String description = image.getBrief();
        String picPath = image.getStoreFileO();
        String picMiddlePath = image.getStoreFileS();
        String picSmallPath = image.getStoreFileT();

        xuan.put("title", title);
        xuan.put("description", description);
        xuan.put("author", author);
        xuan.put("originUrl", originUrl);
        xuan.put("tag", tag);
        xuan.put("categoryId", categoryId);
        xuan.put("picPath", picPath);
        xuan.put("picMiddlePath", picMiddlePath);
        xuan.put("picSmallPath", picSmallPath);
        xuan.put("picDesc", description);

        //推相片的时候推送相片至相册下 contentId使用相册ID
        xuan.put("contentId", albumId);
    }

    /**
     * 生成播客推送炫知消息
     *
     * @param xuan
     * @param contentId
     */
    public void gen40Message(Map xuan, String contentId) {
        //TODO
        // 获取 article对象
        VideoArticle video = videoArticleService.getVideoArticle(Integer.valueOf(contentId));
        if(Lang.isEmpty(video)){
            logger.error("查询不到播客 VideoArticle 对象 contentId：" + contentId);
            xuan.clear();
            return;
        }

        String title = video.getTitle();
        String author = video.getNickName();
        String originUrl = ArticleHelper.makeVideoURL(video);
        String tag = video.getTagWords();
        int categoryId = video.getSystemCategoryId();
        if (!StringUtils.isEmpty(tag)) {
            tag = tag.replaceAll(" ", ",");
        }

        String description = video.getBrief();
        String videoUrl = video.getURL();

        xuan.put("title", title);
        xuan.put("description", description);
        xuan.put("author", author);
        xuan.put("originUrl", originUrl);
        xuan.put("tag", tag);
        xuan.put("categoryId", categoryId);
        xuan.put("videoUrl", videoUrl);
    }


    @Override
    public String[] genXML(Map message) {
        // 取出必要的字段
        Integer appId = (Integer) message.get("appId");
        String contentId = (String) message.get("contentId");
        String editorName = (String) message.get("editorName");
        // 根据不同应用生成xml文件
        String[] filePathArr = null;
        if (appId == null) {
            logger.error("参数异常,appId == null");
            return null;
        }
        if (appId == 20) {
            filePathArr = genBlogXml(appId, contentId, editorName);
        } else if (appId == 30) {
            filePathArr = genPhotoXml(appId, contentId, editorName);
        } else if (appId == 40) {
            filePathArr = genVideoXml(appId, contentId, editorName);
        }

        return filePathArr;
    }

    @Override
    public String[] genCNML(Map message) {
        // 取出必要的字段
        Integer appId = (Integer) message.get("appId");
        String contentId = (String) message.get("contentId");
        String editorName = (String) message.get("editorName");
        // 根据不同应用生成xml文件
        String[] filePaths = null;
        if (appId == null) {
            logger.error("参数异常,appId == null");
            return null;
        }
        if (appId == 20) {
            filePaths = genBlogCNML(appId, contentId, editorName);
        } else if (appId == 30) {
            filePaths = genPhotoCNML(appId, contentId, editorName);
        } else if (appId == 40) {
            filePaths = genVideoCNML(appId, contentId, editorName);
        }


        return filePaths;
    }

    @Override
    public void callbackUGC(Map message) {
        logger.info("通知原系统该消息已推CMS...");
        // 取出必要的字段
        String contentId = (String) message.get("contentId");
        String column = (String) message.get("column");
        Integer userId = 0;
        if (null != message.get("editorId")) {
            userId = (Integer) message.get("editorId");
        }
        String nickName = "审核平台";
        if (null != message.get("editorNickName")) {
            nickName = (String) message.get("editorNickName");
        }
        String clientAddress = "0.0.0.0";
        if (null != message.get("editorIp")) {
            clientAddress = (String) message.get("editorIp");
        }
        Integer appId = (Integer) message.get("appId");
        if (appId == null) {
            return;
        }
        try{

            if (appId == 20) {
                Map callbackPara = new HashMap();
                callbackPara.put("articleId", String.valueOf(contentId));
                callbackPara.put("userId", String.valueOf(userId));
                callbackPara.put("nickname", nickName);
                callbackPara.put("clientAddress", clientAddress);
                articleManager.afterPush2CMS(callbackPara);
            } else if(appId ==30 && column.equals("2")) {
                imageService.exportCms(Integer.valueOf(contentId));
            }else {
                logger.info("无需callback原UGC系统");
            }
        }catch (Exception e){
            logger.error("通知原系统该消息已推CMS失败!appId="+appId+",contentId="+String.valueOf(contentId)+e, e);
        }
        logger.info("通知完成!appId="+appId+",contentId="+String.valueOf(contentId));
    }


    /**
     * 生成博客上传CMS的XML文件，并返回文件路径
     *
     * @param appId
     * @param contentId
     * @param editorName
     * @return
     * @throws IOException
     */
    public String[] genBlogXml(Integer appId, String contentId, String editorName) {
        String filePath = null;
        // 获取 article对象
        Article article = articleManager.getArticle(Integer.valueOf(contentId));
        logger.info("获取到的article:" + JSON.toJSONString(article));
        // 生成文件名
        String guid = ArticleHelper.makeGUID(article.getId());
        // 生成 XML
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("Document");
        rootElement.addElement("SystemID").setText("01");
        rootElement.addElement("SubSystemID").setText("10");
        rootElement.addElement("GUID").setText(guid);
        rootElement.addElement("ID").setText(String.valueOf(article.getId()));
        rootElement.addElement("Action").setText("01");
        rootElement.addElement("Title").addCDATA(ArticleHelper.escapeHtml(article.getTitle()));
        // 截取前300字节长度的内容作为abstract
        String articleAbstract = article.getBrief();
        if (articleAbstract == null) {
            articleAbstract = "";
        }
        if (articleAbstract.length() > 300) {
            articleAbstract = articleAbstract.substring(0, 300);
        }
        rootElement.addElement("Abstract").addCDATA(ArticleHelper.escapeHtml(articleAbstract));
        rootElement.addElement("Keywords").addCDATA(article.getKeywords());
        rootElement.addElement("Author").addCDATA("<a href=http://" + article.getUserName() + ".home.news.cn/blog/>" + article.getNickName() + "</a>");
        rootElement.addElement("Editor").addText(String.valueOf(editorName));
        rootElement.addElement("Source");
        rootElement.addElement("Link").addCDATA(ArticleHelper.makePermanentURL(article));
        // 下载图片
        String imgUrl = article.getDetailImage();
        File imgFile = ArticleHelper.url2File(imgUrl, imgDirPath);
        if (imgFile != null) {
            char[] imgData = ArticleHelper.fetchBASE64Image(imgFile);
            if (imgData != null && imgData.length > 0) {
                rootElement.addElement("ThumbImage").addCDATA(String.valueOf(imgData));
            } else {
                rootElement.addElement("ThumbImage");
            }
        } else {
            rootElement.addElement("ThumbImage");
        }
        // 取标签名
        int categoryId = article.getSystemCategoryId();
        String categoryName = articleSysCategoryManager.getCategoryNameById(categoryId);
        if (StringUtils.isEmpty(categoryName)) {
            categoryName = "杂谈";
        }
        rootElement.addElement("Category").addAttribute("id", String.valueOf(categoryId)).addCDATA(categoryName);
        rootElement.addElement("CreatedTime").addText(ArticleHelper.formatTimeInXML(article.getCreatedTime()));
        rootElement.addElement("CheckUser");
        rootElement.addElement("CheckedTime").addText(ArticleHelper.formatTimeInXML(article.getPostTime()));
        rootElement.addElement("Content").addCDATA(ArticleHelper.removeInvalidXMLChar(article.getContentHtml()));
        rootElement.addElement("BloggerURL").addCDATA("<a href=http://" + article.getUserName() + ".home.news.cn/blog/ target=_blank class=tt_5>" + article.getNickName() + "</a>");
        rootElement.addElement("ArticleType").addText(String.valueOf(article.getType()));

        // 保存xml文件
        filePath = filePath(guid, localPath);
        weiteDocument(document, filePath);

        String[] filePathArr = new String[2];
        filePathArr[0] = filePath;
        return filePathArr;
    }

    /**
     * 生成博客上传CMS的CNML文件，并返回文件路径
     *
     * @param appId
     * @param contentId
     * @param editorName
     * @return
     * @throws IOException
     */
    private String[] genBlogCNML(Integer appId, String contentId, String editorName) {
        String[] filePathArr = new String[2];
        // 获取 article对象
        Article article = articleManager.getArticle(Integer.valueOf(contentId));
        logger.info("获取到的article:" + JSON.toJSONString(article));
        // 生成文件名
        String guid = ArticleHelper.makeGUID(article.getId()) + "_new";
        // 生成 XML
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("CNML");
        root.addAttribute("schemaVersion", "1.0");
        root.addAttribute("template", "cnml_xinhua");
        root.addAttribute("templateVersion", "1.0.0");
        root.addAttribute("xmlns", "http://www.cnml.org.cn/2005/CNMLSchema");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.addAttribute("xsi:schemaLocation", "http://www.cnml.org.cn/2005/CNMLSchema ./CNML_v1.020080229.xsd");

        Element eleEnvelop = root.addElement("Envelop");

        Element eleSystemId = eleEnvelop.addElement("SystemId");
        eleSystemId.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-SystemId-1:1");
        eleSystemId.addAttribute("topicRef", "en");
        eleSystemId.addElement("Name").setText(guid);

        eleEnvelop.addElement("TransferTime").setText(ArticleHelper.formatTimeInCNML(new Date()));

        Element eleSentFrom = eleEnvelop.addElement("SentFrom");
        eleSentFrom.addAttribute("xsi:type", "OrganizationType");
        Element eleNameTopic = eleEnvelop.addElement("NameTopic");
        eleNameTopic.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Organization-1:1");
        eleNameTopic.addAttribute("topicRef", "Xinhua");
        eleNameTopic.addElement("Name").setText("博客系统");

        Element eleSentTo = eleEnvelop.addElement("SentTo");
        Element eleGroupRecipient = eleSentTo.addElement("GroupRecipient");
        eleGroupRecipient.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-pProduct-33:33");
        eleGroupRecipient.addAttribute("topicRef", "rteph");
        eleGroupRecipient.addElement("Name").setText("博客系统送内容管理系统");
        Element eleProductId = eleGroupRecipient.addElement("ProductId");
        eleProductId.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-pProduct-33:33");
        eleProductId.addAttribute("topicRef", "rteph");
        eleGroupRecipient.addElement("DailySequence").setText("1");

        Element eleServices = eleEnvelop.addElement("Services");
        Element eleService = eleServices.addElement("Service");
        eleService.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Service-1:1");
        eleService.addAttribute("topicRef", "semifinished");

        Element eleProducts = eleEnvelop.addElement("Products");
        Element eleProduct = eleProducts.addElement("Product");
        eleProduct.addAttribute("productID", "boxt");
        Element eleProductName = eleProduct.addElement("ProductName");
        eleProductName.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-tProduct-1:1");
        eleProductName.addAttribute("topicRef", "rteph");
        eleProductName.addElement("Name").setText("博客系统");

        Element eleColumns = eleProduct.addElement("Columns");
        Element eleColumn = eleColumns.addElement("Column");
        eleColumn.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Column-1:1");

        int categoryId = article.getSystemCategoryId();
        String categoryName = articleSysCategoryManager.getCategoryNameById(categoryId);
        if (StringUtils.isEmpty(categoryName)) {
            categoryName = "杂谈";
        }

        eleColumn.addAttribute("topicRef", "001");
        eleColumn.addElement("Name").setText(categoryName);

        Element eleItems = root.addElement("Items");
        Element eleItem = eleItems.addElement("Item");
        eleItem.addAttribute("xsi:type", "NewsItemType");
        Element eleMetaInfo = eleItem.addElement("MetaInfo");

        Element eleMetaGroup = eleMetaInfo.addElement("MetaGroup");
        eleItem.addAttribute("xsi:type", "ProcessMetaGroupType");
        Element eleProcessMetadata = eleMetaGroup.addElement("ProcessMetadata");
        Element eleProcessRecord = eleProcessMetadata.addElement("ProcessRecord");
        Element eleProcessRecordItem = eleProcessRecord.addElement("ProcessRecord");
        eleItem.addAttribute("HowPresent", "建稿");
        Element eleProcessor = eleProcessRecordItem.addElement("Processor");
        Element eleOrganization = eleProcessor.addElement("Organization");
        eleOrganization.addAttribute("Scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Department-1:1");
        eleOrganization.addAttribute("topicRef", "Gjlfsh");
        eleProcessor.addElement("Person").addElement("Name").setText("前尘旧梦");
        Element eleOperation = eleProcessRecordItem.addElement("Operation");
        eleOperation.addAttribute("Scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Operation-1:1");
        eleOperation.addAttribute("topicRef", "Establish");
        eleProcessRecordItem.addElement("DateTime").setText(ArticleHelper.formatTimeInCNML(new Date()));

        Element eleMetaGroup2 = eleMetaInfo.addElement("MetaGroup");
        eleMetaGroup2.addAttribute("xsi:type", "XinhuaAdminstrativeMetaGroupType");
        eleMetaGroup2.addElement("IsDirectSend").setText("0");

        Element eleMetaGroupA = eleMetaInfo.addElement("AdministrationMetaGroup");
        Element eleId = eleMetaGroupA.addElement("Id");
        eleId.addElement("PublicId").setText("urn:CNML:xinhua.org:20070701:XxjdvhC000003_20070214_BJTFN0:1");
        eleId.addElement("ProviderId").setText("xinhua.org");
        eleId.addElement("DateId").setText("20070701");
        eleId.addElement("ItemId").setText("XxjdvhC000003_20070214_BJTFN0");
        eleId.addElement("VersionId").setText("1");
        eleMetaGroupA.addElement("Status").setText("Usable");
        Element eleProvider = eleMetaGroupA.addElement("Provider");
        eleProvider.addAttribute("xsi:type", "OrganizationType");
        eleProvider.addElement("Name").setText("新华社");
        eleMetaGroupA.addElement("FirstCreateTime").setText(ArticleHelper.formatTimeInCNML(article.getCreatedTime()));
        eleMetaGroupA.addElement("CurrentRevisionTime").setText(ArticleHelper.formatTimeInCNML(article.getPostTime()));
        Element eleProcessPriority = eleMetaGroupA.addElement("ProcessPriority");
        eleProcessPriority.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-ProcessPriority-1:1");
        eleProcessPriority.addAttribute("topicRef", "3.0");
        Element eleUrgency = eleMetaGroupA.addElement("Urgency");
        eleUrgency.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Urgency-1:1");
        eleUrgency.addAttribute("topicRef", "2.0");
        Element eleImportance = eleMetaGroupA.addElement("Importance");
        eleImportance.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Importance-1:1");
        eleImportance.addAttribute("topicRef", "1.0");

        Element eleMetaGroupD = eleMetaInfo.addElement("DescriptionMetaGroup");

        Element eleTitles = eleMetaGroupD.addElement("Titles");
        eleTitles.addElement("HeadLine").addCDATA(article.getTitle());
        eleTitles.addElement("SubHeadLine").addCDATA("");
        Element eleSubHeadLine = eleTitles.addElement("SubHeadLine");
        eleSubHeadLine.addAttribute("xml:lang", "en");
        StringBuffer sbAlrticleLink = new StringBuffer(128);
        sbAlrticleLink.append("<a href=");
        sbAlrticleLink.append(articleManager.makePermanentURL(article));
        sbAlrticleLink.append(">");
        sbAlrticleLink.append(article.getUserName());
        sbAlrticleLink.append("</a>");
        eleSubHeadLine.addCDATA(sbAlrticleLink.toString());

        Element eleLanguage = eleMetaGroupD.addElement("Language");
        eleLanguage.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Language-1:1");
        eleLanguage.addAttribute("topicRef", "zh-CN");

        StringBuffer sbBloggerLink = new StringBuffer(128);
        sbBloggerLink.append("<a href=http://");
        sbBloggerLink.append(article.getUserName());
        sbBloggerLink.append(".home.news.cn/blog/ target=_blank class=tt_5>");
        sbBloggerLink.append(article.getNickName());
        sbBloggerLink.append("</a>");

        Element eleCreators = eleMetaGroupD.addElement("Creators");
        Element eleCreator = eleCreators.addElement("Creator");
        eleCreator.addAttribute("kind", "Author");
        eleCreator.addAttribute("xsi:type", "PersonType");
        Element eleCreatorName = eleCreator.addElement("Name");
        Element eleFullName = eleCreatorName.addElement("FullName");
        eleFullName.addAttribute("xml:lang", "zh-CN");
        eleFullName.addCDATA(sbBloggerLink.toString());
        Element eleElectronicAddress = eleCreator.addElement("ElectronicAddress");
        eleElectronicAddress.addElement("Email");
        eleElectronicAddress.addElement("URL").addCDATA(sbBloggerLink.toString());

        Element eleDateTimes = eleMetaGroupD.addElement("DateTimes");
        Element eleDateTime = eleDateTimes.addElement("DateTime");
        eleDateTime.addAttribute("kind", "InditeTime");
        eleDateTime.setText(new java.sql.Date(article.getCreatedTime().getTime()).toString());

        eleMetaGroupD.addElement("Abstract").addCDATA(article.getBrief());

        Element eleSubjectCodes = eleMetaGroupD.addElement("SubjectCodes");
        Element eleSubjectCode = eleSubjectCodes.addElement("SubjectCode");
        eleSubjectCode.addAttribute("kind", "XH_Internalinternational");
        Element eleMainCode = eleSubjectCodes.addElement("MainCode");
        eleMainCode.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-XH_Internalinternational-1:1");
        eleMainCode.addAttribute("topicRef", "01");
        eleMainCode.addElement("Name").setText("国内");

        Element eleKeywords = eleMetaGroupD.addElement("Keywords");
        Element eleKeyword = eleKeywords.addElement("Keyword");
        eleKeyword.addCDATA(article.getTagWords());

        eleMetaGroupD.addElement("SourceInfo");
        eleMetaGroupD.addElement("Locations");

        Element eleContents = eleItem.addElement("Contents");
        Element eleContentText = eleContents.addElement("ContentItem");
        Element eleContentImage = eleContents.addElement("ContentItem");

        eleContentText.addAttribute("id", "c01");
        eleContentText.addAttribute("xsi:type", "TextCIType");
        eleContentText.addElement("DataContent").addCDATA(ArticleHelper.removeInvalidXMLChar(article.getContentHtml()));

        String imgFileName = guid + "_c02.jpg";
        String imgFilePath = imgDirPath.concat("/" + imgFileName);
        File imgFile = new File(imgFilePath);

        // 图片处理
        String imgUrl = article.getDetailImage();
        if (!StringUtils.isEmpty(imgUrl)) {
            String realPath = imgDirPath.concat(ArticleHelper.getFileName(imgUrl));
            File downloadedImgFile = new File(realPath);
            if (downloadedImgFile.exists()) {
                try {
                    FileUtils.moveFile(downloadedImgFile, imgFile);
                } catch (IOException e) {
                    logger.error("重命名文件出错！", e);
                }
            }
        } else {
            File defaultFile = new File(defaultImg);
            try {
                FileUtils.copyFile(defaultFile, imgFile);
            } catch (IOException e) {
                logger.error("拷贝默认文件出错！", e);
            }
        }


        eleContentImage.addAttribute("id", "c02");
        eleContentImage.addAttribute("href", imgFileName);
        eleContentImage.addAttribute("xsi:type", "ImageCIType");
        Element eleImageMetaInfo = eleContentImage.addElement("MetaInfo");
        Element eleIMetaGroupC = eleImageMetaInfo.addElement("CharacteristicMetaGroup");
        eleIMetaGroupC.addAttribute("xsi:type", "ImageCIMetaGroupType");
        Element eleFormat = eleIMetaGroupC.addElement("Format");
        eleFormat.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Format-1:1");
        eleFormat.addAttribute("topicRef", "JPG");
        Element eleMediaType = eleIMetaGroupC.addElement("MediaType");
        eleMediaType.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-MediaType-1:1");
        eleMediaType.addAttribute("topicRef", "Photo");
        eleIMetaGroupC.addElement("SizeInBytes").setText(String.valueOf(imgFile.length()));
        eleIMetaGroupC.addElement("ColorType").setText("Color");
        eleIMetaGroupC.addElement("Orientation").setText("Landscape");
        eleIMetaGroupC.addElement("PixelWidth").setText("800");
        eleIMetaGroupC.addElement("PixelHeight").setText("600");
        eleIMetaGroupC.addElement("Resolution").setText("72");
        eleIMetaGroupC.addElement("FilmNumber").setText("0000000");
        eleContentImage.addElement("DataContent").addCDATA("");

        // 保存xml文件
        String xmlfilePath = filePath(guid, localPath);
        weiteDocument(document, xmlfilePath);

        filePathArr[0] = xmlfilePath;
        filePathArr[1] = imgFilePath;

        return filePathArr;
    }

    /**
     * 将xml document对象写入硬盘，并返回文件路径
     *
     * @param document
     * @param filePath
     * @throws IOException
     */
    private final static void weiteDocument(Document document, String filePath) {
        Writer fileWriter = null;
        XMLWriter xmlWriter = null;
        try {
            // 如果不存在则自动创建
            File dir = new File(filePath.substring(0, filePath.lastIndexOf("/")));
            if (!dir.exists() && !dir.isDirectory()) {
                dir.mkdir();
            }
            fileWriter = new FileWriter(filePath);
            OutputFormat format = OutputFormat.createPrettyPrint();
            xmlWriter = new XMLWriter(fileWriter, format);
            xmlWriter.write(document);
        } catch (IOException e) {
            logger.error("XML文件保存失败!", e);
        } finally {
            try {
                xmlWriter.flush();
                xmlWriter.close();
            } catch (IOException e) {
                logger.error("xmlWriter关闭失败!", e);
            }
        }
        logger.info("xml文件已生成:" + filePath);
    }

    private final static String filePath(String guid, String localPath) {
        StringBuilder sb = new StringBuilder(localPath);
        sb.append("/");
        sb.append(guid);
        sb.append(".xml");
        return sb.toString();
    }

    /**
     * 生成拍客上传CMS的XML文件，并返回文件路径
     *
     * @param appId
     * @param contentId
     * @param editorName
     * @return
     * @throws IOException
     */
    public String[] genPhotoXml(Integer appId, String contentId, String editorName) {


        String filePath = null;
        // 获取 ImageVO
        ImageVO image = imageService.get(Integer.valueOf(contentId));
        logger.info("获取到的image:" + JSON.toJSONString(image));
        // 生成文件名
        String guid = ArticleHelper.makeGUID(image.getId());
        // 生成 XML
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("Document");
        rootElement.addElement("SystemID").setText("01");
        rootElement.addElement("SubSystemID").setText("04");
        rootElement.addElement("GUID").setText(guid);
        rootElement.addElement("ID").setText(String.valueOf(image.getId()));
        rootElement.addElement("Action").setText("01");
        rootElement.addElement("Title").addCDATA(ArticleHelper.escapeHtml(image.getTitle()));
        // 截取前300字节长度的内容作为abstract
// 截取前300字节长度的内容作为abstract
        String imageAbstract = image.getBrief();
        if (imageAbstract == null) {
            imageAbstract = "";
        }
        if (imageAbstract.length() > 300) {
            imageAbstract = imageAbstract.substring(0, 300);
        }
        // 开始输出XML的正文

        rootElement.addElement("Abstract").addCDATA(ArticleHelper.escapeHtml(imageAbstract));
        rootElement.addElement("Keywords").addCDATA(ArticleHelper.escapeHtml(image.getTagWords()));
        rootElement.addElement("Author").addCDATA("<a href=http://" + image.getUserName() + ".home.news.cn/photo/>" + image.getNickName() + "</a>");
        rootElement.addElement("Editor").addText(String.valueOf(editorName));
        rootElement.addElement("Source");
        rootElement.addElement("Link").addCDATA(ArticleHelper.makePhotoURL(image));

        // 下载图片
        String imgUrl = ArticleHelper.getPhotoCompletionURL(image.getStoreFileO());
        File imgFile = ArticleHelper.url2File(imgUrl, imgDirPath);
        if (imgFile != null) {
            char[] imgData = ArticleHelper.fetchBASE64Image(imgFile);
            if (imgData != null && imgData.length > 0) {
                rootElement.addElement("ThumbImage").addCDATA(String.valueOf(imgData));
            } else {
                rootElement.addElement("ThumbImage");
            }
        } else {
            rootElement.addElement("ThumbImage");
        }

        // 取类别名
        int categoryId = image.getSysCatId();
        String categoryName = image.getCateName();
        if (categoryName == null) {
            categoryName = String.valueOf(image.getSysCatId());
        }
        rootElement.addElement("Category").addAttribute("id", String.valueOf(categoryId)).addCDATA(categoryName);

        rootElement.addElement("CreatedTime").addText(ArticleHelper.formatTimeInXML(image.getCreateTime()));
        rootElement.addElement("CheckUser");
        rootElement.addElement("CheckedTime").addText(ArticleHelper.formatTimeInXML(image.getCreateTime()));

        rootElement.addElement("Content").addCDATA(ArticleHelper.removeInvalidXMLChar(image.getBrief()));
        rootElement.addElement("BloggerURL").addCDATA("<a href=http://" + image.getUserName() + ".home.news.cn/photo/ target=_blank class=tt_5>" + image.getNickName() + "</a>");
        rootElement.addElement("ArticleType").addText("0");
        // 保存xml文件
        filePath = filePath(guid, localPath);
        weiteDocument(document, filePath);

        String[] filePathArr = new String[2];
        filePathArr[0] = filePath;
        return filePathArr;


        /**** */
    }

    /**
     * 生成拍客上传CMS的CNML文件，并返回文件路径
     *
     * @param appId
     * @param contentId
     * @param editorName
     * @return
     * @throws IOException
     */
    private String[] genPhotoCNML(Integer appId, String contentId, String editorName) {
        String[] filePathArr = new String[2];

        // 获取 image
        ImageVO image = imageService.get(Integer.valueOf(contentId));
        logger.info("获取到的image:" + JSON.toJSONString(image));
        // 生成文件名
        String guid = ArticleHelper.makeGUID(image.getId()) + "_new";

        // 生成 XML
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("CNML");
        root.addAttribute("schemaVersion", "1.0");
        root.addAttribute("template", "cnml_xinhua");
        root.addAttribute("templateVersion", "1.0.0");
        root.addAttribute("xmlns", "http://www.cnml.org.cn/2005/CNMLSchema");
        root.addAttribute("xmlns:xsi",
                "http://www.w3.org/2001/XMLSchema-instance");
        root.addAttribute("xsi:schemaLocation",
                "http://www.cnml.org.cn/2005/CNMLSchema ./CNML_v1.020080229.xsd");

        Element eleEnvelop = root.addElement("Envelop");

        Element eleSystemId = eleEnvelop.addElement("SystemId");
        eleSystemId.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-SystemId-1:1");
        eleSystemId.addAttribute("topicRef", "en");
        eleSystemId.addElement("Name").setText(guid);

        eleEnvelop.addElement("TransferTime").setText(ArticleHelper.formatTimeInCNML(new Date()));

        Element eleSentFrom = eleEnvelop.addElement("SentFrom");
        eleSentFrom.addAttribute("xsi:type", "OrganizationType");
        Element eleNameTopic = eleEnvelop.addElement("NameTopic");
        eleNameTopic.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-Organization-1:1");
        eleNameTopic.addAttribute("topicRef", "Xinhua");
        eleNameTopic.addElement("Name").setText("博客系统");

        Element eleSentTo = eleEnvelop.addElement("SentTo");
        Element eleGroupRecipient = eleSentTo.addElement("GroupRecipient");
        eleGroupRecipient.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-pProduct-33:33");
        eleGroupRecipient.addAttribute("topicRef", "rteph");
        eleGroupRecipient.addElement("Name").setText("博客系统送内容管理系统");
        Element eleProductId = eleGroupRecipient.addElement("ProductId");
        eleProductId.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-pProduct-33:33");
        eleProductId.addAttribute("topicRef", "rteph");
        eleGroupRecipient.addElement("DailySequence").setText("1");

        Element eleServices = eleEnvelop.addElement("Services");
        Element eleService = eleServices.addElement("Service");
        eleService.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-Service-1:1");
        eleService.addAttribute("topicRef", "semifinished");

        Element eleProducts = eleEnvelop.addElement("Products");
        Element eleProduct = eleProducts.addElement("Product");
        eleProduct.addAttribute("productID", "boxt");
        Element eleProductName = eleProduct.addElement("ProductName");
        eleProductName.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-tProduct-1:1");
        eleProductName.addAttribute("topicRef", "rteph");
        eleProductName.addElement("Name").setText("拍客系统");

        Element eleColumns = eleProduct.addElement("Columns");
        Element eleColumn = eleColumns.addElement("Column");
        eleColumn.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-Column-1:1");

        String categoryName = image.getCateName();
        if (Lang.isEmpty(categoryName)) {
            categoryName = "其他";
        }
        eleColumn.addAttribute("topicRef", "001");
        eleColumn.addElement("Name").setText(categoryName);

        Element eleItems = root.addElement("Items");
        Element eleItem = eleItems.addElement("Item");
        eleItem.addAttribute("xsi:type", "NewsItemType");
        Element eleMetaInfo = eleItem.addElement("MetaInfo");

        Element eleMetaGroup = eleMetaInfo.addElement("MetaGroup");
        eleItem.addAttribute("xsi:type", "ProcessMetaGroupType");
        Element eleProcessMetadata = eleMetaGroup.addElement("ProcessMetadata");
        Element eleProcessRecord = eleProcessMetadata
                .addElement("ProcessRecord");
        Element eleProcessRecordItem = eleProcessRecord
                .addElement("ProcessRecord");
        eleItem.addAttribute("HowPresent", "建稿");
        Element eleProcessor = eleProcessRecordItem.addElement("Processor");
        Element eleOrganization = eleProcessor.addElement("Organization");
        eleOrganization.addAttribute("Scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-Department-1:1");
        eleOrganization.addAttribute("topicRef", "Gjlfsh");
        eleProcessor.addElement("Person").addElement("Name").setText("前尘旧梦");
        Element eleOperation = eleProcessRecordItem.addElement("Operation");
        eleOperation.addAttribute("Scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-Operation-1:1");
        eleOperation.addAttribute("topicRef", "Establish");
        eleProcessRecordItem.addElement("DateTime").setText(ArticleHelper.formatTimeInCNML(new Date()));

        Element eleMetaGroup2 = eleMetaInfo.addElement("MetaGroup");
        eleMetaGroup2.addAttribute("xsi:type",
                "XinhuaAdminstrativeMetaGroupType");
        eleMetaGroup2.addElement("IsDirectSend").setText("0");

        Element eleMetaGroupA = eleMetaInfo
                .addElement("AdministrationMetaGroup");
        Element eleId = eleMetaGroupA.addElement("Id");
        eleId.addElement("PublicId").setText(
                "urn:CNML:xinhua.org:20070701:XxjdvhC000003_20070214_BJTFN0:1");
        eleId.addElement("ProviderId").setText("xinhua.org");
        eleId.addElement("DateId").setText("20070701");
        eleId.addElement("ItemId").setText("XxjdvhC000003_20070214_BJTFN0");
        eleId.addElement("VersionId").setText("1");
        eleMetaGroupA.addElement("Status").setText("Usable");
        Element eleProvider = eleMetaGroupA.addElement("Provider");
        eleProvider.addAttribute("xsi:type", "OrganizationType");
        eleProvider.addElement("Name").setText("新华社");
        eleMetaGroupA.addElement("FirstCreateTime").setText(ArticleHelper.formatTimeInCNML(image.getCreateTime()));
        eleMetaGroupA.addElement("CurrentRevisionTime").setText(ArticleHelper.formatTimeInCNML(image.getCreateTime()));
        Element eleProcessPriority = eleMetaGroupA
                .addElement("ProcessPriority");
        eleProcessPriority
                .addAttribute("scheme",
                        "urn:cnml:xinhua.org:20070701:topiclist.cnml-ProcessPriority-1:1");
        eleProcessPriority.addAttribute("topicRef", "3.0");
        Element eleUrgency = eleMetaGroupA.addElement("Urgency");
        eleUrgency.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-Urgency-1:1");
        eleUrgency.addAttribute("topicRef", "2.0");
        Element eleImportance = eleMetaGroupA.addElement("Importance");
        eleImportance.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-Importance-1:1");
        eleImportance.addAttribute("topicRef", "1.0");

        Element eleMetaGroupD = eleMetaInfo.addElement("DescriptionMetaGroup");

        Element eleTitles = eleMetaGroupD.addElement("Titles");
        eleTitles.addElement("HeadLine").addCDATA(image.getTitle());
        Element eleSubHeadLine = eleTitles.addElement("SubHeadLine");
        eleSubHeadLine.addAttribute("xml:lang", "en");

        StringBuffer sbPhotoImageLink = new StringBuffer(128);
        sbPhotoImageLink.append("<a href=http://");
        sbPhotoImageLink.append(image.getUserName());
        sbPhotoImageLink.append(".home.news.cn/photo/view.do?id=").append(
                image.getId());
        sbPhotoImageLink.append(">");
        sbPhotoImageLink.append(image.getUserName());
        sbPhotoImageLink.append("</a>");

        eleSubHeadLine.addCDATA(sbPhotoImageLink.toString());

        Element eleLanguage = eleMetaGroupD.addElement("Language");
        eleLanguage.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-Language-1:1");
        eleLanguage.addAttribute("topicRef", "zh-CN");

        StringBuffer sbBloggerLink = new StringBuffer(128);
        sbBloggerLink.append("<a href=http://");
        sbBloggerLink.append(image.getUserName());
        sbBloggerLink.append(".home.news.cn/photo/ target=_blank class=tt_5>");
        sbBloggerLink.append(image.getNickName());
        sbBloggerLink.append("</a>");

        Element eleCreators = eleMetaGroupD.addElement("Creators");
        Element eleCreator = eleCreators.addElement("Creator");
        eleCreator.addAttribute("kind", "Author");
        eleCreator.addAttribute("xsi:type", "PersonType");
        Element eleCreatorName = eleCreator.addElement("Name");
        Element eleFullName = eleCreatorName.addElement("FullName");
        eleFullName.addAttribute("xml:lang", "zh-CN");
        // eleFullName.setText(image.getNickName());
        eleFullName.addCDATA(sbBloggerLink.toString());
        Element eleElectronicAddress = eleCreator
                .addElement("ElectronicAddress");
        eleElectronicAddress.addElement("Email");
        eleElectronicAddress.addElement("URL").addCDATA(
                sbBloggerLink.toString());

        Element eleDateTimes = eleMetaGroupD.addElement("DateTimes");
        Element eleDateTime = eleDateTimes.addElement("DateTime");
        eleDateTime.addAttribute("kind", "InditeTime");
        eleDateTime.setText(new java.sql.Date(image.getCreateTime().getTime())
                .toString());

        eleMetaGroupD.addElement("Abstract").addCDATA(image.getBrief());

        Element eleSubjectCodes = eleMetaGroupD.addElement("SubjectCodes");
        Element eleSubjectCode = eleSubjectCodes.addElement("SubjectCode");
        eleSubjectCode.addAttribute("kind", "XH_Internalinternational");
        Element eleMainCode = eleSubjectCodes.addElement("MainCode");
        eleMainCode
                .addAttribute("scheme",
                        "urn:cnml:xinhua.org:20070701:topiclist.cnml-XH_Internalinternational-1:1");
        eleMainCode.addAttribute("topicRef", "01");
        eleMainCode.addElement("Name").setText("国内");

        Element eleKeywords = eleMetaGroupD.addElement("Keywords");
        Element eleKeyword = eleKeywords.addElement("Keyword");
        eleKeyword.addCDATA(image.getTagWords());

        eleMetaGroupD.addElement("SourceInfo");
        eleMetaGroupD.addElement("Locations");

        Element eleContents = eleItem.addElement("Contents");
        Element eleContentText = eleContents.addElement("ContentItem");
        Element eleContentImage = eleContents.addElement("ContentItem");

        eleContentText.addAttribute("id", "c01");
        eleContentText.addAttribute("xsi:type", "TextCIType");


        String imgFileName = guid + "_c02.jpg";
        String imgFilePath = imgDirPath.concat("/" + imgFileName);
        File imgFile = new File(imgFilePath);

        // 下载图片
        String imgUrl = image.getStoreFileO();

        // 图片处理
        if (!StringUtils.isEmpty(imgUrl)) {
            String realPath = imgDirPath.concat(ArticleHelper.getFileName(imgUrl));
            File downloadedImgFile = new File(realPath);
            if (downloadedImgFile.exists()) {
                try {
                    FileUtils.moveFile(downloadedImgFile, imgFile);
                } catch (IOException e) {
                    logger.error("重命名文件出错！", e);
                }
            }
        } else {
            File defaultFile = new File(defaultImg);
            try {
                FileUtils.copyFile(defaultFile, imgFile);
            } catch (IOException e) {
                logger.error("拷贝默认文件出错！", e);
            }
        }


        eleContentImage.addAttribute("id", "c02");
        eleContentImage.addAttribute("href", imgFileName);
        eleContentImage.addAttribute("xsi:type", "ImageCIType");
        Element eleImageMetaInfo = eleContentImage.addElement("MetaInfo");
        Element eleIMetaGroupC = eleImageMetaInfo
                .addElement("CharacteristicMetaGroup");
        eleIMetaGroupC.addAttribute("xsi:type", "ImageCIMetaGroupType");
        Element eleFormat = eleIMetaGroupC.addElement("Format");
        eleFormat.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-Format-1:1");
        eleFormat.addAttribute("topicRef", "JPG");
        Element eleMediaType = eleIMetaGroupC.addElement("MediaType");
        eleMediaType.addAttribute("scheme",
                "urn:cnml:xinhua.org:20070701:topiclist.cnml-MediaType-1:1");
        eleMediaType.addAttribute("topicRef", "Photo");
        eleIMetaGroupC.addElement("SizeInBytes").setText(String.valueOf(imgFile.length()));
        eleIMetaGroupC.addElement("ColorType").setText("Color");
        eleIMetaGroupC.addElement("Orientation").setText("Landscape");
        eleIMetaGroupC.addElement("PixelWidth").setText("800");
        eleIMetaGroupC.addElement("PixelHeight").setText("600");
        eleIMetaGroupC.addElement("Resolution").setText("72");
        eleIMetaGroupC.addElement("FilmNumber").setText("0000000");
        // String data = new String(fetchBASE64Image(fileImage));
        eleContentImage.addElement("DataContent").addCDATA("");



        // 保存xml文件
        String xmlfilePath = filePath(guid, localPath);
        weiteDocument(document, xmlfilePath);

        filePathArr[0] = xmlfilePath;
        filePathArr[1] = imgFilePath;

        return filePathArr;
    }

    /**
     * 生成拍客上传CMS的XML文件，并返回文件路径
     *
     * @param appId
     * @param contentId
     * @param editorName
     * @return
     * @throws IOException
     */
    public String[] genVideoXml(Integer appId, String contentId, String editorName) {
        String filePath = null;
        // 获取 VideoArticle 对象
        VideoArticle article = videoArticleService.getVideoArticle(Integer.valueOf(contentId));
        logger.info("获取到的VideoArticle:" + JSON.toJSONString(article));
        // 生成文件名
        String guid = ArticleHelper.makeGUID(article.getId());

        // 生成 XML
        Document document = DocumentHelper.createDocument();
        Element rootElement = document.addElement("Document");
        rootElement.addElement("SystemID").setText("01");
        rootElement.addElement("SubSystemID").setText("02");
        rootElement.addElement("GUID").setText(guid);
        rootElement.addElement("ID").setText(String.valueOf(article.getId()));
        rootElement.addElement("Action").setText("01");
        rootElement.addElement("Title").addCDATA(ArticleHelper.escapeHtml(article.getTitle()));
        // 截取前300字节长度的内容作为abstract
        String articleAbstract = article.getBrief();
        if (articleAbstract == null) {
            articleAbstract = "";
        }
        if (articleAbstract.length() > 300) {
            articleAbstract = articleAbstract.substring(0, 300);
        }
        rootElement.addElement("Abstract").addCDATA(ArticleHelper.escapeHtml(articleAbstract));
        rootElement.addElement("Keywords").addCDATA(ArticleHelper.escapeHtml(article.getTagWords()));
        rootElement.addElement("Author").addCDATA("<a href=http://" + article.getUserName() + ".home.news.cn/video/>" + article.getNickName() + "</a>");
        rootElement.addElement("Editor").addText(String.valueOf(editorName));
        rootElement.addElement("Source");
        rootElement.addElement("Link").addCDATA(ArticleHelper.makeVideoURL(article));


        // 下载图片
        String imgUrl = article.getCloudFileImg();
        File imgFile = ArticleHelper.url2File(imgUrl, imgDirPath);
        if (imgFile != null) {
            char[] imgData = ArticleHelper.fetchBASE64Image(imgFile);
            if (imgData != null && imgData.length > 0) {
                rootElement.addElement("ThumbImage").addCDATA(String.valueOf(imgData));
            } else {
                rootElement.addElement("ThumbImage");
            }
        } else {
            rootElement.addElement("ThumbImage");
        }



        String categoryName = "";
        if (article.getSystemCategory() != null) {
            categoryName = article.getSystemCategory().getTitle();
        }
        rootElement.addElement("Category").addAttribute("id", String.valueOf(article.getSystemCategoryId())).addCDATA(categoryName);

        rootElement.addElement("CreatedTime").addText(ArticleHelper.formatTimeInXML(article.getCreatedTime()));
        rootElement.addElement("CheckUser");
        rootElement.addElement("CheckedTime").addText(ArticleHelper.formatTimeInXML(article.getPostTime()));

        rootElement.addElement("Content").addCDATA(ArticleHelper.removeInvalidXMLChar(article.getBrief()));
        rootElement.addElement("BloggerURL").addCDATA("<a href=http://" + article.getUserName() + ".home.news.cn/video/ target=_blank class=tt_5>" + article.getNickName() + "</a>");

        rootElement.addElement("ArticleType").addText("2");
        rootElement.addElement("MultiAttach").addCDATA(article.getURL() + ".flv");

        // 保存xml文件
        filePath = filePath(guid, localPath);
        weiteDocument(document, filePath);

        String[] filePathArr = new String[2];
        filePathArr[0] = filePath;
        return filePathArr;

    }

    /**
     * 生成拍客上传CMS的CNML文件，并返回文件路径
     *
     * @param appId
     * @param contentId
     * @param editorName
     * @return
     * @throws IOException
     */
    private String[] genVideoCNML(Integer appId, String contentId, String editorName) {
        String[] filePathArr = new String[2];
        // 获取 article对象
        VideoArticle article = videoArticleService.getVideoArticle(Integer.valueOf(contentId));
        logger.info("获取到的VideoArticle:" + JSON.toJSONString(article));
        // 生成文件名
        String guid = ArticleHelper.makeGUID(article.getId()) + "_new";


        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("CNML");
        root.addAttribute("schemaVersion", "1.0");
        root.addAttribute("template", "cnml_xinhua");
        root.addAttribute("templateVersion", "1.0.0");
        root.addAttribute("xmlns", "http://www.cnml.org.cn/2005/CNMLSchema");
        root.addAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
        root.addAttribute("xsi:schemaLocation", "http://www.cnml.org.cn/2005/CNMLSchema ./CNML_v1.020080229.xsd");

        Element eleEnvelop = root.addElement("Envelop");

        Element eleSystemId = eleEnvelop.addElement("SystemId");
        eleSystemId.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-SystemId-1:1");
        eleSystemId.addAttribute("topicRef", "en");
        eleSystemId.addElement("Name").setText(guid);

        eleEnvelop.addElement("TransferTime").setText(ArticleHelper.formatTimeInCNML(new Date()));

        Element eleSentFrom = eleEnvelop.addElement("SentFrom");
        eleSentFrom.addAttribute("xsi:type", "OrganizationType");
        Element eleNameTopic = eleEnvelop.addElement("NameTopic");
        eleNameTopic.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Organization-1:1");
        eleNameTopic.addAttribute("topicRef", "Xinhua");
        eleNameTopic.addElement("Name").setText("博客系统");

        Element eleSentTo = eleEnvelop.addElement("SentTo");
        Element eleGroupRecipient = eleSentTo.addElement("GroupRecipient");
        eleGroupRecipient.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-pProduct-33:33");
        eleGroupRecipient.addAttribute("topicRef", "rteph");
        eleGroupRecipient.addElement("Name").setText("博客系统送内容管理系统");
        Element eleProductId = eleGroupRecipient.addElement("ProductId");
        eleProductId.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-pProduct-33:33");
        eleProductId.addAttribute("topicRef", "rteph");
        eleGroupRecipient.addElement("DailySequence").setText("1");

        Element eleServices = eleEnvelop.addElement("Services");
        Element eleService = eleServices.addElement("Service");
        eleService.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Service-1:1");
        eleService.addAttribute("topicRef", "semifinished");

        Element eleProducts = eleEnvelop.addElement("Products");
        Element eleProduct = eleProducts.addElement("Product");
        eleProduct.addAttribute("productID", "boxt");
        Element eleProductName = eleProduct.addElement("ProductName");
        eleProductName.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-tProduct-1:1");
        eleProductName.addAttribute("topicRef", "rteph");
        eleProductName.addElement("Name").setText("播客系统");

        Element eleColumns = eleProduct.addElement("Columns");
        Element eleColumn = eleColumns.addElement("Column");
        eleColumn.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Column-1:1");

        String categoryName = null;
        VideoCategory category = article.getSystemCategory();
        if (category != null) {
            categoryName = category.getTitle();
        } else {
            categoryName = "其他";
        }

        eleColumn.addAttribute("topicRef", "001");
        eleColumn.addElement("Name").setText(categoryName);

        Element eleItems = root.addElement("Items");
        Element eleItem = eleItems.addElement("Item");
        eleItem.addAttribute("xsi:type", "NewsItemType");
        Element eleMetaInfo = eleItem.addElement("MetaInfo");

        Element eleMetaGroup = eleMetaInfo.addElement("MetaGroup");
        eleItem.addAttribute("xsi:type", "ProcessMetaGroupType");
        Element eleProcessMetadata = eleMetaGroup.addElement("ProcessMetadata");
        Element eleProcessRecord = eleProcessMetadata.addElement("ProcessRecord");
        Element eleProcessRecordItem = eleProcessRecord.addElement("ProcessRecord");
        eleItem.addAttribute("HowPresent", "建稿");
        Element eleProcessor = eleProcessRecordItem.addElement("Processor");
        Element eleOrganization = eleProcessor.addElement("Organization");
        eleOrganization.addAttribute("Scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Department-1:1");
        eleOrganization.addAttribute("topicRef", "Gjlfsh");
        eleProcessor.addElement("Person").addElement("Name").setText("前尘旧梦");
        Element eleOperation = eleProcessRecordItem.addElement("Operation");
        eleOperation.addAttribute("Scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Operation-1:1");
        eleOperation.addAttribute("topicRef", "Establish");
        eleProcessRecordItem.addElement("DateTime").setText(ArticleHelper.formatTimeInCNML(new Date()));

        Element eleMetaGroup2 = eleMetaInfo.addElement("MetaGroup");
        eleMetaGroup2.addAttribute("xsi:type", "XinhuaAdminstrativeMetaGroupType");
        eleMetaGroup2.addElement("IsDirectSend").setText("0");

        Element eleMetaGroupA = eleMetaInfo.addElement("AdministrationMetaGroup");
        Element eleId = eleMetaGroupA.addElement("Id");
        eleId.addElement("PublicId").setText("urn:CNML:xinhua.org:20070701:XxjdvhC000003_20070214_BJTFN0:1");
        eleId.addElement("ProviderId").setText("xinhua.org");
        eleId.addElement("DateId").setText("20070701");
        eleId.addElement("ItemId").setText("XxjdvhC000003_20070214_BJTFN0");
        eleId.addElement("VersionId").setText("1");
        eleMetaGroupA.addElement("Status").setText("Usable");
        Element eleProvider = eleMetaGroupA.addElement("Provider");
        eleProvider.addAttribute("xsi:type", "OrganizationType");
        eleProvider.addElement("Name").setText("新华社");
        eleMetaGroupA.addElement("FirstCreateTime").setText(ArticleHelper.formatTimeInCNML(article.getCreatedTime()));
        eleMetaGroupA.addElement("CurrentRevisionTime").setText(ArticleHelper.formatTimeInCNML(article.getPostTime()));
        Element eleProcessPriority = eleMetaGroupA.addElement("ProcessPriority");
        eleProcessPriority.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-ProcessPriority-1:1");
        eleProcessPriority.addAttribute("topicRef", "3.0");
        Element eleUrgency = eleMetaGroupA.addElement("Urgency");
        eleUrgency.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Urgency-1:1");
        eleUrgency.addAttribute("topicRef", "2.0");
        Element eleImportance = eleMetaGroupA.addElement("Importance");
        eleImportance.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Importance-1:1");
        eleImportance.addAttribute("topicRef", "1.0");

        Element eleMetaGroupD = eleMetaInfo.addElement("DescriptionMetaGroup");

        Element eleTitles = eleMetaGroupD.addElement("Titles");
        eleTitles.addElement("HeadLine").addCDATA(article.getTitle());
        eleTitles.addElement("SubHeadLine").addCDATA("");
        Element eleSubHeadLine = eleTitles.addElement("SubHeadLine");
        eleSubHeadLine.addAttribute("xml:lang", "en");
        StringBuffer sbAlrticleLink = new StringBuffer(128);
        sbAlrticleLink.append("<a href=");
        sbAlrticleLink.append(ArticleHelper.makeVideoURL(article));
        sbAlrticleLink.append(">");
        sbAlrticleLink.append(article.getUserName());
        sbAlrticleLink.append("</a>");
        eleSubHeadLine.addCDATA(sbAlrticleLink.toString());

        Element eleLanguage = eleMetaGroupD.addElement("Language");
        eleLanguage.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Language-1:1");
        eleLanguage.addAttribute("topicRef", "zh-CN");

        StringBuffer sbBloggerLink = new StringBuffer(128);
        sbBloggerLink.append("<a href=http://");
        sbBloggerLink.append(article.getUserName());
        sbBloggerLink.append(".home.news.cn/blog/ target=_blank class=tt_5>");
        sbBloggerLink.append(article.getNickName());
        sbBloggerLink.append("</a>");

        Element eleCreators = eleMetaGroupD.addElement("Creators");
        Element eleCreator = eleCreators.addElement("Creator");
        eleCreator.addAttribute("kind", "Author");
        eleCreator.addAttribute("xsi:type", "PersonType");
        Element eleCreatorName = eleCreator.addElement("Name");
        Element eleFullName = eleCreatorName.addElement("FullName");
        eleFullName.addAttribute("xml:lang", "zh-CN");
        eleFullName.addCDATA(sbBloggerLink.toString());
        Element eleElectronicAddress = eleCreator.addElement("ElectronicAddress");
        eleElectronicAddress.addElement("Email");
        eleElectronicAddress.addElement("URL").addCDATA(sbBloggerLink.toString());

        Element eleDateTimes = eleMetaGroupD.addElement("DateTimes");
        Element eleDateTime = eleDateTimes.addElement("DateTime");
        eleDateTime.addAttribute("kind", "InditeTime");
        eleDateTime.setText(new java.sql.Date(article.getCreatedTime().getTime()).toString());

        eleMetaGroupD.addElement("Abstract").addCDATA(article.getBrief());

        Element eleSubjectCodes = eleMetaGroupD.addElement("SubjectCodes");
        Element eleSubjectCode = eleSubjectCodes.addElement("SubjectCode");
        eleSubjectCode.addAttribute("kind", "XH_Internalinternational");
        Element eleMainCode = eleSubjectCodes.addElement("MainCode");
        eleMainCode.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-XH_Internalinternational-1:1");
        eleMainCode.addAttribute("topicRef", "01");
        eleMainCode.addElement("Name").setText("国内");

        Element eleKeywords = eleMetaGroupD.addElement("Keywords");
        Element eleKeyword = eleKeywords.addElement("Keyword");
        eleKeyword.addCDATA(article.getTagWords());

        eleMetaGroupD.addElement("SourceInfo");
        eleMetaGroupD.addElement("Locations");

        Element eleContents = eleItem.addElement("Contents");
        Element eleContentText = eleContents.addElement("ContentItem");
        Element eleContentImage = eleContents.addElement("ContentItem");

        eleContentText.addAttribute("id", "c01");
        eleContentText.addAttribute("xsi:type", "TextCIType");


// File fileImage = getDetailImage(article);
// String filename = guid + "_c02.jpg";
// File fileTemp = getTempFile(filename);
// FileUtil.copy(fileImage, fileTemp);
// list.add(fileTemp);

        String imgFileName = guid + "_c02.jpg";
        String imgFilePath = imgDirPath.concat("/" + imgFileName);
        File imgFile = new File(imgFilePath);
        // 图片处理
        String imgUrl = article.getCloudFileImg();
        if (!StringUtils.isEmpty(imgUrl)) {
            String realPath = imgDirPath.concat(ArticleHelper.getFileName(imgUrl));
            File downloadedImgFile = new File(realPath);
            if (downloadedImgFile.exists()) {
                try {
                    FileUtils.moveFile(downloadedImgFile, imgFile);
                } catch (IOException e) {
                    logger.error("重命名文件出错！", e);
                }
            }
        } else {
            File defaultFile = new File(defaultImg);
            try {
                FileUtils.copyFile(defaultFile, imgFile);
            } catch (IOException e) {
                logger.error("拷贝默认文件出错！", e);
            }
        }






        eleContentImage.addAttribute("id", "c02");
        eleContentImage.addAttribute("href", imgFileName);
        eleContentImage.addAttribute("xsi:type", "ImageCIType");
        Element eleImageMetaInfo = eleContentImage.addElement("MetaInfo");
        Element eleIMetaGroupC = eleImageMetaInfo.addElement("CharacteristicMetaGroup");
        eleIMetaGroupC.addAttribute("xsi:type", "ImageCIMetaGroupType");
        Element eleFormat = eleIMetaGroupC.addElement("Format");
        eleFormat.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-Format-1:1");
        eleFormat.addAttribute("topicRef", "JPG");
        Element eleMediaType = eleIMetaGroupC.addElement("MediaType");
        eleMediaType.addAttribute("scheme", "urn:cnml:xinhua.org:20070701:topiclist.cnml-MediaType-1:1");
        eleMediaType.addAttribute("topicRef", "Photo");
        eleIMetaGroupC.addElement("SizeInBytes").setText(String.valueOf(imgFile.length()));
        eleIMetaGroupC.addElement("ColorType").setText("Color");
        eleIMetaGroupC.addElement("Orientation").setText("Landscape");
        eleIMetaGroupC.addElement("PixelWidth").setText("800");
        eleIMetaGroupC.addElement("PixelHeight").setText("600");
        eleIMetaGroupC.addElement("Resolution").setText("72");
        eleIMetaGroupC.addElement("FilmNumber").setText("0000000");
        eleContentImage.addElement("DataContent").addCDATA("");

        // 保存xml文件
        String xmlfilePath = filePath(guid, localPath);
        weiteDocument(document, xmlfilePath);

        filePathArr[0] = xmlfilePath;
        filePathArr[1] = imgFilePath;

        return filePathArr;
    }

}
