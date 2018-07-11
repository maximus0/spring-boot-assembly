package com.xinhuanet.censor.util;

import cn.news.photo.domain.Album;
import cn.news.photo.domain.Image;
import com.trs.blog.domain.Article;
import com.trs.util.io.BASE64EncoderStream;
import com.trs.util.io.ByteArrayOutputStream;
import com.xinhuanet.video.domain.VideoArticle;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.*;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by conanca on 15-6-5.
 */
public class ArticleHelper {
    private static Logger logger = LoggerFactory.getLogger(ArticleHelper.class);

    private final static String GUID_PATTERN = "0110000000{0,date,yyyyMMdd}1{1,number,00000000}";
    private final static MessageFormat GUID_FORMATOR = new MessageFormat(GUID_PATTERN);


    public final static String makeGUID(int id) {
        StringBuffer sb = new StringBuffer(64);
        Object[] arguments = new Object[4];
        arguments[0] = new Date();
        arguments[1] = new Integer(id);
        GUID_FORMATOR.format(arguments, sb, null);
        return sb.toString();
    }

    public final static String escapeHtml(String src) {
        if (src == null) {
            return "";
        }
        char[] buff = src.toCharArray();
        StringBuffer sb = new StringBuffer(buff.length + 100);
        for (int j = 0; j < buff.length; j++) {
            char c = buff[j];
            switch (c) {
                case '<':
                    sb.append("&lt;");
                    break;
                case '>':
                    sb.append("&gt;");
                    break;
                case '"':
                    sb.append("&quot;");
                    break;
                case '&':
                    sb.append("&amp;");
                    break;
                case 165:
                    sb.append("&yen;");
                    break;
                case 169:
                    sb.append("&copy;");
                    break;
                case 174:
                    sb.append("&reg;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return sb.toString();
    }

    private final static int caculateCheckSum(int hash, int pid, int aid) {
        return (hash + (hash >> 13)) | (pid - (pid >> 12)) & (aid ^ (aid >> 11));
    }

    public final static URLMap valueOf(String sUserName, int iParentId, int iId, int iType) {
        URLMap map = new URLMap();
        map.m_iVersion = URLMap.DEFAULT_VERSION;
        map.m_sUserName = sUserName;
        map.m_iType = iType;
        map.m_iParentId = iParentId;
        map.m_iId = iId;
        map.m_iCheckSum = caculateCheckSum(sUserName.hashCode(), iParentId, iId);
        return map;
    }

    public static String makePermanentURL(Article article) {
        String userName = article.getUserName();
        String fileName = URLMap.valueOf(userName, 0, article.getId(), 1)
                .toString();

        StringBuffer sb = new StringBuffer(128);
        sb.append("http://");
        sb.append(userName);
        sb.append(".home.news.cn/blog/a/");
        sb.append(fileName);
        sb.append(".html");

        return sb.toString();
    }

    public static String makePhotoURL(Image image) {
        String userName = image.getUserName();
        String imageId = String.valueOf(image.getId());

        StringBuffer sb = new StringBuffer(128);
        sb.append("http://");
        sb.append(userName);
        sb.append(".home.news.cn/photo/view.do?id=");
        sb.append(imageId);

        return sb.toString();
    }
    public static String makeAlbumURL(Album album) {
        String albumId = String.valueOf(album.getId());
       String  userName = album.getUserName();

        StringBuffer sb = new StringBuffer(128);
        sb.append("http://");
        sb.append(userName);
        sb.append(".home.news.cn/photo/view.do?id=");
        sb.append(albumId);
        sb.append("/1/10");

        return sb.toString();
    }
    public static String makeVideoURL(VideoArticle video) {
        String userName = video.getUserName();
        String id = String.valueOf(video.getId());

        StringBuffer sb = new StringBuffer(128);
        sb.append("http://");
        sb.append(userName);
        sb.append(".home.news.cn/portal/video/");
        sb.append(id);

        return sb.toString();
    }


    public static String getPhotoCompletionURL(String  imageurl) {

        StringBuffer sb = new StringBuffer(128);
        sb.append("http://tpic.home.news.cn/xhBlog/");
        sb.append(imageurl);


        return sb.toString();
    }
    /**
     * 根据url获取文件名
     *
     * @param imgUrl
     * @return
     */
    public static String getFileName(String imgUrl) {
        if (StringUtils.isEmpty(imgUrl)) {
            return null;
        } else {
            return imgUrl.substring(imgUrl.lastIndexOf("/"));
        }
    }

    /**
     * 将图片写到 硬盘指定目录下
     *
     * @param in
     * @param dirPath
     * @param filePath
     */
    private static File savePicToDisk(InputStream in, String dirPath,
                                      String filePath) {
        File file = null;
        try {
            File dir = new File(dirPath);
            if (dir == null || !dir.exists()) {
                dir.mkdirs();
            }
            //文件真实路径
            String realPath = dirPath.concat(filePath);
            file = new File(realPath);
            if (file == null || !file.exists()) {
                file.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
            fos.close();
        } catch (IOException e) {
            logger.error("图片保存失败!", e);
        } finally {
            try {
                in.close();
            } catch (Exception e) {
                logger.error("流关闭失败!", e);
            }
        }
        return file;
    }

    /**
     * 将指定的url的图片下载并保存
     *
     * @param imgUrl
     * @param imgDirPath
     * @return
     * @throws IOException
     */
    public static File url2File(String imgUrl, String imgDirPath) {
        if(StringUtils.isEmpty(imgUrl)||StringUtils.isEmpty(imgDirPath)){
            return null;
        }
        logger.info("正在下载图片：" + imgUrl);
        File file = null;
        String filePath = getFileName(imgUrl);
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(imgUrl);
        CloseableHttpResponse response1 = null;

        // 设置代理
        HttpHost proxy = new HttpHost("192.168.84.107", 8888, "http");
        RequestConfig config = RequestConfig.custom()
                .setProxy(proxy).setSocketTimeout(5000).setConnectTimeout(5000)
                .build();
        httpGet.setConfig(config);

        try {
            response1 = httpclient.execute(httpGet);
            logger.info(response1.getStatusLine().toString());
            HttpEntity entity1 = response1.getEntity();
            InputStream in = entity1.getContent();
            file = savePicToDisk(in, imgDirPath, filePath);
            System.out.println("保存图片 " + filePath + " 成功....");
            EntityUtils.consume(entity1);
        } catch (ClientProtocolException e) {
           logger.error("图片下载失败！",e);
        } catch (IOException e) {
            logger.error("图片下载失败！", e);
        } finally {
            try {
                response1.close();
            } catch (Exception e) {
                logger.error("response关闭失败！",e);
            }
        }
        return file;
    }

    public static char[] fetchBASE64Image(File file) {
        FileInputStream is = null;
        try {
            if (!file.exists()) {
                return null;
            }
            if (file.length() > 1024 * 500) {
                return null;
            }
            int outLength = (((int) file.length() + 2) / 3) * 4;

            is = new FileInputStream(file);
            int length = 0;
            byte[] tempBuff = new byte[4096];

            byte[] buffer = new byte[outLength];
            ByteArrayOutputStream baos = new ByteArrayOutputStream(buffer);
            BASE64EncoderStream beos = new BASE64EncoderStream(baos);
            while ((length = is.read(tempBuff)) != -1) {
                beos.write(tempBuff, 0, length);
            }
            beos.close();

            char[] data = new char[outLength];
            for (int i = 0; i < outLength; i++) {
                data[i] = (char) buffer[i];
            }
            return data;
        } catch (Exception ex) {
            logger.error("装载图片时发生错误", ex);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ex) {
                    // Ignore
                }
            }
        }
        return null;
    }

    public final static String removeInvalidXMLChar(String src) {
        if (src == null) {
            return "";
        }

        char[] buff = src.toCharArray();
        int length = buff.length;
        boolean removed = false;
        for (int j = 0; j < length; j++) {
            char c = buff[j];

            if (c < 0x20) {
                if (c != '\t' && c != '\r' && c != '\n') {
                    buff[j] = ' ';
                    removed = true;
                }
                continue;
            }
            if (c <= 0xD7FF) {
                continue;
            }
            if (c < 0xE000) {
                buff[j] = ' ';
                removed = true;
                continue;
            }
            if (c <= 0xFFFD) {
                continue;
            }
            if (c < 0x10000) {
                buff[j] = ' ';
                removed = true;
                continue;
            }
            if (c <= 0x10FFFF) {
                continue;
            }

            buff[j] = ' ';
            removed = true;
        }

        if (removed) {
            return new String(buff);
        }
        return src;
    }

    public static String formatTimeInXML(Date date) {
        if(Lang.isEmpty(date)){
            return "";
        }
        return new SimpleDateFormat("yyyy/MM/dd HH:mm:SS").format(date);
    }

    public static String formatTimeInCNML(Date date) {
        if(Lang.isEmpty(date)){
            return "";
        }
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(date) + "+08:00";
    }
}
