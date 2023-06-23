package model;

public class RawData {
    private String code;
    private String data;

    public RawData() {
    	
    }
    
    public RawData(String code, String data) {
    	this.code = code;
    	this.data = data;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RawData{" + "code=" + code + ", data=" + data + '}';
    }
    
    
}
