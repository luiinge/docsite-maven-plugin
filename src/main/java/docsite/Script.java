package docsite;

public class Script {

    private String src;
    private boolean async = false;
    private String code;


    public static Script fromCode (String code) {
        return new Script().code(code);
    }

    public static Script fromSrc(String src) {
        return new Script().src(src);
    }


    public Script src(String src) {
        this.src = src;
        return this;
    }


    public Script async(boolean async) {
        this.async = async;
        return this;
    }


    public Script code(String code) {
        this.code = code;
        return this;
    }

    public String src() {
        return src;
    }


    public boolean async() {
        return async;
    }


    public String code() {
        return code;
    }

}
