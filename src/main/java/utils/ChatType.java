package utils;

public enum ChatType {

    REGISTER, SINGLESEND, SENDINFOS, NOTIFYONLINE, NOTIFYDOWN,GROUPSEND, FILE_MSG_SINGLE_SENDING, FILE_MSG_GROUP_SENDING;
     
    public static void main(String[] args) {
        System.out.println(ChatType.REGISTER);
    }
}
