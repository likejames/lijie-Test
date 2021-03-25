package lijie.test.dao;

public class CallInfo {
    private String uuid="test-03";
    private String userId="8w0L4nceZp";
    private String tenantId="8w0L4nceZp";
    private String callId="test-03";
    private String taskId="test-03";
    private String lineId="1";
    private String isTest="0";
    private String flowToken="3a2edcebc79fd75f5fcaeb581db6af86";
    private String callBack="1";
    private String flowId="6618b2c1f01745da6414e8fe6c863911";
    private String taskType="";
    private String isInterrupt="True";
    private String gatewayName="lalala2";
    private String phone="17320029877";
    private String synthesisInfo;
//    private String flowId;
//
//    public String getFlowId() {
//        return flowId;
//    }
//
//    public void setFlowId(String flowId) {
//        this.flowId = flowId;
//    }

    public CallInfo(){}

    public CallInfo(String uuid,String userId,String tenantId,String callId, String taskId, String lineId, String isTest,
                              String flowToken,String callBack,String taskType,String isInterrupt,String gatewayName, String phone){
        this.uuid = uuid;
        this.userId = userId;
        this.tenantId = tenantId;
        this.callId = callId;
        this.taskId = taskId;
        this.lineId = lineId;
        this.isTest = isTest;
        this.flowToken = flowToken;
        this.callBack =callBack;
        this.taskType = taskType;
        this.isInterrupt = isInterrupt;
        this.gatewayName = gatewayName;
        this.phone = phone;

    }
    private String fsIp;
    private Integer fsPort;

    public String getFsIp() {
        return fsIp;
    }

    public void setFsIp(String fsIp) {
        this.fsIp = fsIp;
    }

    public Integer getFsPort() {
        return fsPort;
    }

    public void setFsPort(Integer fsPort) {
        this.fsPort = fsPort;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    public String getCallId() {
        return callId;
    }

    public void setCallId(String callId) {
        this.callId = callId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getLineId() {
        return lineId;
    }

    public void setLineId(String lineId) {
        this.lineId = lineId;
    }

    public String getIsTest() {
        return isTest;
    }

    public void setIsTest(String isTest) {
        this.isTest = isTest;
    }

    public String getFlowToken() {
        return flowToken;
    }

    public void setFlowToken(String flowToken) {
        this.flowToken = flowToken;
    }

    public String getCallBack() {
        return callBack;
    }

    public void setCallBack(String callBack) {
        this.callBack = callBack;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getIsInterrupt() {
        return isInterrupt;
    }

    public void setIsInterrupt(String isInterrupt) {
        this.isInterrupt = isInterrupt;
    }

    public String getGatewayName() {
        return gatewayName;
    }

    public void setGatewayName(String gatewayName) {
        this.gatewayName = gatewayName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public String getSynthesisInfo() {
        return synthesisInfo;
    }

    public void setSynthesisInfo(String synthesisInfo) {
        this.synthesisInfo = synthesisInfo;
    }
}
