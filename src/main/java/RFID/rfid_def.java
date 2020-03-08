package RFID;

public class rfid_def
{
	static public final int VER_WINDOWS = 1;
	static public final int VER_LINUX = 2;
	static public final int AR_X86 = 32;
	static public final int AR_X64 = 64;

	public static final String LOADED_RDRDVR_OPT_CATALOG = "CATALOG";
	public static final String LOADED_RDRDVR_OPT_NAME = "NAME";
	public static final String LOADED_RDRDVR_OPT_ID = "ID";
	public static final String LOADED_RDRDVR_OPT_COMMTYPESUPPORTED = "COMM_TYPE_SUPPORTED";
	public static final String RDRDVR_TYPE_READER = "Reader";// general reader

	public static final String CONNSTR_NAME_RDTYPE = "RDType";
	public static final String CONNSTR_NAME_COMMTYPE = "CommType";

	public static final String CONNSTR_NAME_COMMTYPE_COM = "COM";
	public static final String CONNSTR_NAME_COMMTYPE_USB = "USB";
	public static final String CONNSTR_NAME_COMMTYPE_NET = "NET";

	// HID Param
	public static final String CONNSTR_NAME_HIDADDRMODE = "AddrMode";
	public static final String CONNSTR_NAME_HIDSERNUM = "SerNum";
	// COM Param
	public static final String CONNSTR_NAME_COMNAME = "COMName";
	public static final String CONNSTR_NAME_COMBARUD = "BaudRate";
	public static final String CONNSTR_NAME_COMFRAME = "Frame";
	public static final String CONNSTR_NAME_BUSADDR = "BusAddr";
	// TCP,UDP
	public static final String CONNSTR_NAME_REMOTEIP = "RemoteIP";
	public static final String CONNSTR_NAME_REMOTEPORT = "RemotePort";
	public static final String CONNSTR_NAME_LOCALIP = "LocalIP";

	public static final byte AI_TYPE_NEW = 1;// new antenna inventory (reset RF
												// power)
	public static final byte AI_TYPE_CONTINUE = 2;// continue antenna inventory
													// ;

	// Move position
	public static final byte RFID_NO_SEEK = 0;// No seeking
	public static final byte RFID_SEEK_FIRST = 1;// Seek first
	public static final byte RFID_SEEK_NEXT = 2;// Seek next
	public static final byte RFID_SEEK_LAST = 3;// Seek last

	// Air protocol id
	public static final int RFID_APL_UNKNOWN_ID = 0;
	public static final int RFID_APL_ISO15693_ID = 1;
	public static final int RFID_APL_ISO14443A_ID = 2;

	// ISO15693 Tag type id
	public static final int RFID_UNKNOWN_PICC_ID = 0;
	public static final int RFID_ISO15693_PICC_ICODE_SLI_ID = 1;
	public static final int RFID_ISO15693_PICC_TI_HFI_PLUS_ID = 2;
	public static final int RFID_ISO15693_PICC_ST_M24LRXX_ID = 3;/* ST M24 serial */
	public static final int RFID_ISO15693_PICC_FUJ_MB89R118C_ID = 4;
	public static final int RFID_ISO15693_PICC_ST_M24LR64_ID = 5;
	public static final int RFID_ISO15693_PICC_ST_M24LR16E_ID = 6;
	public static final int RFID_ISO15693_PICC_ICODE_SLIX_ID = 7;
	public static final int RFID_ISO15693_PICC_TIHFI_STANDARD_ID = 8;
	public static final int RFID_ISO15693_PICC_TIHFI_PRO_ID = 9;
	// ISO14443a tag type id
	public static final int RFID_ISO14443A_PICC_NXP_ULTRALIGHT_ID = 1;
	public static final int RFID_ISO14443A_PICC_NXP_MIFARE_S50_ID = 2;
	public static final int RFID_ISO14443A_PICC_NXP_MIFARE_S70_ID = 3;

	public static final int INVEN_STOP_TRIGGER_TYPE_Tms = 0;
	public static final int INVEN_STOP_TRIGGER_TYPE_N_attempt = 1;
	public static final int INVEN_STOP_TRIGGER_TYPE_N_found = 2;
	public static final int INVEN_STOP_TRIGGER_TYPE_TIMEOUT = 3;

	// iso18000p6C session
	public static final byte ISO18000p6C_S0 = 0;
	public static final byte ISO18000p6C_S1 = 1;
	public static final byte ISO18000p6C_S2 = 2;
	public static final byte ISO18000p6C_S3 = 3;
	// ISO18000p6C Memory bank
	public static final long ISO18000p6C_MEM_BANK_RFU = 0x00;
	public static final long ISO18000p6C_MEM_BANK_EPC = 0x01;
	public static final long ISO18000p6C_MEM_BANK_TID = 0x02;
	public static final long ISO18000p6C_MEM_BANK_USER = 0x03;
	// iso18000p6c target
	public static final byte ISO18000p6C_TARGET_A = 0x00;
	public static final byte ISO18000p6C_TARGET_B = 0x01;

	// iso18000p6C Q
	public static final byte ISO18000p6C_Dynamic_Q = (byte) 0xff;

	public static final long ISO18000p6C_META_BIT_MASK_EPC = 0x01;
	public static final long ISO18000P6C_META_BIT_MASK_TIMESTAMP = 0x02;
	public static final long ISO18000P6C_META_BIT_MASK_FREQUENCY = 0x04;
	public static final long ISO18000p6C_META_BIT_MASK_RSSI = 0x08;
	public static final long ISO18000P6C_META_BIT_MASK_READCOUNT = 0x10;
	public static final long ISO18000P6C_META_BIT_MASK_TAGDATA = 0x20;

	public static final long ISO18000p6C_SELECT_TARGET_INV_S0 = 0x00;
	public static final long ISO18000p6C_SELECT_TARGET_INV_S1 = 0x01;
	public static final long ISO18000p6C_SELECT_TARGET_INV_S2 = 0x02;
	public static final long ISO18000p6C_SELECT_TARGET_INV_S3 = 0x03;
	public static final long ISO18000p6C_SELECT_TARGET_INV_SL = 0x04;
}
