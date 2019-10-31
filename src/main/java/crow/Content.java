package crow;

import com.alibaba.fastjson.JSONObject;
import crow.bean.Port;
import crow.bean.Routes;
import crow.util.DBUtil;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @BelongsProject: crowdycw
 * @BelongsPackage: org.zj.crow
 * @Author: ZhangJun
 * @CreateTime: 2019/1/5
 * @Description: ${Description}
 */
public class Content {


    static PhantomJSDriver driver;
    private static final String baseUrl = "https://www.iautos.cn";
    private static final String link = "https://www.iautos.cn/chexing/";

    static Map<String, String> headMap = new HashMap<>();

    private static String lastBrandId;

    private static List<Routes> routesList = new ArrayList<>();
    private static List<Port> portList = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        inflateRoute();
        inflatePort();
    }

    private static void inflatePort() {
        File route=new File("D:\\temp\\crawl\\port\\route.json");
        File port=new File("D:\\temp\\crawl\\port\\port.json");
        for (Routes routes : routesList) {
            //进去这个页面
            Element elementBySe = getElementByJsoup(routes.getLink());
            //然后把每一行数据都写进去
            Elements select = elementBySe.select("#wrap > div:nth-child(3) > div > table > tbody > tr");
            for (Element tr : select) {
                Port port1 = new Port();
                //这是每一个tr
                Elements eleTds = tr.select("> td");
                Element elePortCode = eleTds.get(0);
                if (!Objects.isNull(elePortCode)) {
                    Elements select1 = elePortCode.select("> a");
                    if (!Objects.isNull(select1) && !select1.isEmpty()) {
                        String portCode = select1.text();
                        System.out.println(" 港口代码" + portCode);
                        port1.setPortCode(portCode);
                    }
                }
                Element elePortCode1 = eleTds.get(1);
                if (!Objects.isNull(elePortCode1)) {
                    String text = elePortCode1.text();
                    System.out.println(" 这是港口中文名:" + text);
                    port1.setCnName(text);
                }
                Element elePortCode2 = eleTds.get(2);
                if (!Objects.isNull(elePortCode2)) {
                    String portEnName = elePortCode2.text();
                    System.out.println(" 英文名" + portEnName);
                    port1.setEnName(portEnName);
                }
                Element elePortCode3 = eleTds.get(3);
                if (!Objects.isNull(elePortCode3)) {
                    Elements select1 = elePortCode3.select("> a");
                    if (!Objects.isNull(select1) && !select1.isEmpty()) {
                        String belongCountry = select1.text();
                        System.out.println(" 属于" + belongCountry);
                        port1.setBelonCountyEnName(belongCountry);
                    }
                }
                port1.setRouteName(routes.getName());
                writeAppend(new StringBuilder(JSONObject.toJSONString(port1)).append("\r\n"),port);
            }
            //这里写入
            writeAppend(new StringBuilder(JSONObject.toJSONString(routes)).append("\r\n"),route);
        }
    }

    private static void runSql() {
    }


    private static void inflateRoute() {
        Element elementBySe = getElementBySe("https://www.gangkoudaima.com/");
        Elements select = elementBySe.select("#wrap > div:nth-child(4) > div:nth-child(2) > table > tbody > tr");
        if (!Objects.isNull(select) && !select.isEmpty()) {
            for (Element eleTr : select) {
                Elements tds = eleTr.select("> td");
                for (Element td : tds) {
                    Routes routes = new Routes();
                    Elements eleAs = td.select("> a");
                    if (!Objects.isNull(eleAs) && !eleAs.isEmpty()) {
                        Element eleA = eleAs.get(0);
                        routes.setName(eleA.text());
                        routes.setLink("https://www.gangkoudaima.com" + eleA.attr("href"));
                        routesList.add(routes);
                        System.out.println(routes.getName());
                    }
                }
            }
        }
    }


    /**
     * 生成java bean 的方法
     */
    public static void generateJava() {
        StringBuilder sb = new StringBuilder();
        String path = "E:\\dev\\project\\java\\crawltruckhome\\src\\main\\java\\crow\\bean\\CarSubtype.java";
        //这里我要生成java实体
        Element elementBySe = getElementBySe("https://product.360che.com/m280/70095_param.html");
        Elements select = elementBySe.select("#mybody > div.wrapper > div.parameter-detail.highlighted > table:nth-child(1) > tbody > tr[class=param-row]");
        for (Element ele : select) {
            Elements select1 = ele.select(">td:nth-child(1)");
            String text = select1.text().replaceAll("：", "");
            System.out.println(text);
            //开始写入
            sb.append("private String ").append(getFirstSpell(text)).append(";").append("//").append(text).append("\r\n");
        }
        writeAppend(sb, new File(path));
    }


    private static void createTable() throws Exception {
        String brand = genCreateTable(new File("E:\\dev\\project\\java\\crawltruckhome\\src\\main\\java\\crow\\bean\\Brand.java"), "brand");
        String brandSeries = genCreateTable(new File("E:\\dev\\project\\java\\crawltruckhome\\src\\main\\java\\crow\\bean\\BrandSeries.java"), "brand_series");
        String carModel = genCreateTable(new File("E:\\dev\\project\\java\\crawltruckhome\\src\\main\\java\\crow\\bean\\CarModel.java"), "car_model");
        String carSubtype = genCreateTable(new File("E:\\dev\\project\\java\\crawltruckhome\\src\\main\\java\\crow\\bean\\CarSubtype.java"), "car_subtype");

        DBUtil dbUtil = new DBUtil();
        dbUtil.runSql(brand);
        dbUtil.runSql(brandSeries);
        dbUtil.runSql(carModel);
        dbUtil.runSql(carSubtype);
    }

    private static String genCreateTable(File file, String tableName) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder sb = new StringBuilder("create table " + tableName + "(");
        String line = null;
        while ((line = br.readLine()) != null) {
            if (line.contains("//")) {
                String[] split = line.split("//");
                String comment = split[1];
                String[] s = split[0].split(" ");
                System.out.println(Arrays.toString(s));
                String name = s[s.length - 1].replace(";", "");
                sb.append(" ").append(name).append(" varchar(50) comment '").append(comment).append("',");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }


    private static String genInsertSql(String tableName, Object obj) {
        StringBuilder beforeSb = new StringBuilder("insert into " + tableName + "(");

        for (Field f : obj.getClass().getDeclaredFields()) {
            beforeSb.append(f.getName()).append(",");
        }
        beforeSb.deleteCharAt(beforeSb.length() - 1);
        beforeSb.append(") values (");

        StringBuilder afterSb = new StringBuilder();
        for (Field f : obj.getClass().getDeclaredFields()) {
            f.setAccessible(true);
            try {
                afterSb.append("'" + f.get(obj) + "'").append(",");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        afterSb.deleteCharAt(afterSb.length() - 1);
        afterSb.append(")");
        return beforeSb.toString() + afterSb.toString();
    }

    private static Element getElementByJsoup(String url) {
        try {
            return Jsoup.connect(url).get().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void initHeadMap() {

        if (headMap.size() != 0) {
            return;
        }

        headMap.put("Content-Type", "application/json; charset=utf-8");
        headMap.put("Accept", "image/webp,image/apng,image/*,*/*;q=0.8");
        headMap.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
    }


    private static Element getElementBySe(String url) {
        if (driver == null) {
            initDriver();
        }
        //打开页面
        driver.get(url);
        return Jsoup.parse(driver.getPageSource()).body();
    }

    private static void initDriver() {

        //设置必要参数
        DesiredCapabilities dcaps = new DesiredCapabilities();
        //ssl证书支持
        dcaps.setCapability("acceptSslCerts", true);
        //截屏支持
        dcaps.setCapability("takesScreenshot", true);
        //css搜索支持
        dcaps.setCapability("cssSelectorsEnabled", true);
        //js支持
        dcaps.setJavascriptEnabled(true);
        //驱动支持（第二参数表明的是你的phantomjs引擎所在的路径）
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "E:\\dev\\plugin\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        //创建无界面浏览器对象

        driver = new PhantomJSDriver(dcaps);

        //设置隐性等待（作用于全局）
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    private static Element testOne() {
        String url = "https://www.iautos.cn/chexing/trim.asp?id=156034";
        return getElementBySe(url);
    }


    public static void genBean() {
        Element element = testOne();

        List<Element> jbxxs = element.select("#webpage > div:nth-child(12) > div.mainRight > div.jbxx");
        for (Element ele : jbxxs) {

            List<Element> trs = ele.select("> div > table > tbody > tr");

            for (Element e : trs) {

                List<Element> tds = e.select("> td");

                for (int i = 0; i < tds.size(); i += 2) {
                    String name = tds.get(i).text();
                    System.out.println("public String " + getFirstSpell(name) + ";//" + name);
                }
            }
        }

    }


    /**
     * 用于获得汉字的首拼音
     *
     * @param chinese
     * @return
     */
    public static String getFirstSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat);
                    if (temp != null) {
                        pybf.append(temp[0].charAt(0));
                    }
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString().replaceAll("\\W", "").trim();
    }

    /**
     * 将文字写入
     * @param file
     * @param str
     */
    /**
     * 写进去，添加的方式
     *
     * @param sb
     * @param file
     */
    public static void writeAppend(StringBuilder sb, File file) {
        try {
            FileWriter bw = new FileWriter(file, true);
            bw.append(sb);
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
