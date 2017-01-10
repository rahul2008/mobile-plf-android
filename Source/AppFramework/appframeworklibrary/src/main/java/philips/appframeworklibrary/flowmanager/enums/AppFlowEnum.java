package philips.appframeworklibrary.flowmanager.enums;

public enum AppFlowEnum {

    FILE_NOT_FOUND("Json file not found"),
    JSON_PARSE_EXCEPTION("Json parse exception");

    private final String description;

    AppFlowEnum(final String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }
}
