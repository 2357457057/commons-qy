package top.yqingyu.common.qymsg;

/**
 * @author YYJ
 * @version 1.0.0
 * @ClassName top.yqingyu.common.qymsg.Dict
 * @description
 * @createTime 2023年01月05日 23:23:00
 */
interface Dict {
    /*==========================HEADER=============================*/
    Integer HEADER_LENGTH = 8;
    Integer MSG_TYPE_IDX = 0;
    Integer DATA_TYPE_IDX = 1;
    Integer SEGMENTATION_IDX = 2;
    Integer MSG_LENGTH_IDX_START = 4;
    Integer MSG_LENGTH_IDX_END = 8;
    /*==========================HEADER=============================*/


    /*==========================分片信息=============================*/
    Integer PARTITION_ID_IDX_START = 0;
    Integer PARTITION_ID_LENGTH = 16;
    Integer PARTITION_ID_IDX_END = PARTITION_ID_IDX_START + PARTITION_ID_LENGTH;
    Integer NUMERATOR_IDX_START = 16;
    Integer NUMERATOR_LENGTH = 2;
    Integer NUMERATOR_IDX_END = NUMERATOR_IDX_START + NUMERATOR_LENGTH;
    Integer DENOMINATOR_IDX_START = 18;
    Integer DENOMINATOR_LENGTH = 2;
    Integer DENOMINATOR_IDX_END = DENOMINATOR_IDX_START + DENOMINATOR_LENGTH;
    Integer SEGMENTATION_INFO_LENGTH = PARTITION_ID_LENGTH + NUMERATOR_LENGTH + DENOMINATOR_LENGTH;
    /*==========================分片信息=============================*/






    /*==========================QyMSG=============================*/
    Integer CLIENT_ID_LENGTH = 36;
    String QYMSG = "MSG";

    /*==========================QyMSG=============================*/







    /*==========================FileMSG=============================*/
    String FILE_ID = "FILE_ID";
    String FILE_NAME = "FILE_NAME";
    String FILE_LENGTH = "FILE_LENGTH";
    String FILE_LOCAL_PATH = "LOCAL_PATH";
    String FILE_REMOTE_PATH = "REMOTE_PATH";
    String FILE_CUT_TIMES = "CUT_TIMES";
    String FILE_IDX = "IDX";
    String FILE_POSITION = "POSITION";
    String FILE_CUT_LENGTH = "CUT_LENGTH";

    Integer TRANS_THREAD_MAX = 6;

    /*==========================FileMSG=============================*/
}
