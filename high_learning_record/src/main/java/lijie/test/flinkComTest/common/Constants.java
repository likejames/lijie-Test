package lijie.test.flinkComTest.common;

public class Constants {

    public static String fullDateFormatStr = "yyyy-MM-dd hh:mm:ss";

    public static String dayDateFormatStr = "yyyy-MM-dd";


    public static enum ReplyType {

        ANSWER("直接回答"), CLARIFY("澄清"), NOANSWER("未识别");

        private String value;

        ReplyType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public static enum DBType {

        MYSQL("mysql"), ORACLE("oracle");

        private String value;

        DBType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

}
