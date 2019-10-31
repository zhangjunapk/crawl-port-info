package crow.bean;

/**
 * @ProjectName: crawlportinfo
 * @ClassName: Port
 * @Description: 港口
 * @Author: ZhangJun
 * @Date: 2019/10/31 16:33
 */
public class Port {
    private String portCode;//港口代码
    private String cnName;//中文名
    private String enName;//英文名
    private String county;//所属国家中文名
    private String belonCountyEnName;//所属国家英文名
    private String route;//航线
    private String desc;//介绍
    private String routeName;//航线名

    public String getRouteName() {
        return routeName;
    }

    public Port setRouteName(String routeName) {
        this.routeName = routeName;
        return this;
    }

    public String getPortCode() {
        return portCode;
    }

    public Port setPortCode(String portCode) {
        this.portCode = portCode;
        return this;
    }

    public String getCnName() {
        return cnName;
    }

    public Port setCnName(String cnName) {
        this.cnName = cnName;
        return this;
    }

    public String getEnName() {
        return enName;
    }

    public Port setEnName(String enName) {
        this.enName = enName;
        return this;
    }

    public String getCounty() {
        return county;
    }

    public Port setCounty(String county) {
        this.county = county;
        return this;
    }

    public String getBelonCountyEnName() {
        return belonCountyEnName;
    }

    public Port setBelonCountyEnName(String belonCountyEnName) {
        this.belonCountyEnName = belonCountyEnName;
        return this;
    }

    public String getRoute() {
        return route;
    }

    public Port setRoute(String route) {
        this.route = route;
        return this;
    }

    public String getDesc() {
        return desc;
    }

    public Port setDesc(String desc) {
        this.desc = desc;
        return this;
    }
}
