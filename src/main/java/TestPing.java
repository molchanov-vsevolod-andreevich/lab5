public class TestPing {
    private String url;
    private Integer count;

    public ResultPing(String url, Integer ping) {
        this.url = url;
        this.ping = ping;
    }

    public String getUrl() {
        return url;
    }

    public Integer getPing() {
        return ping;
    }
}
