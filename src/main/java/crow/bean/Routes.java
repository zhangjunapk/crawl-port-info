package crow.bean;

/**
 * @ProjectName: crawlportinfo
 * @ClassName: Routes
 * @Description: 航线
 * @Author: ZhangJun
 * @Date: 2019/10/31 16:31
 */

public class Routes {
    private String name;
    private String link;

    public String getName() {
        return name;
    }

    public Routes setName(String name) {
        this.name = name;
        return this;
    }

    public String getLink() {
        return link;
    }

    public Routes setLink(String link) {
        this.link = link;
        return this;
    }
}
