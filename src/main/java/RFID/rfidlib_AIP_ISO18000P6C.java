package RFID;

//javah -classpath . -jni com.fridge.api.FridgeLockCtrol
public class rfidlib_AIP_ISO18000P6C
{
	public static void LoadLib(String sLibPath, int osType, int arType)
	{
		String osName = "";
		String libPath = "";
		
		String architecture = "";
		if (osType == rfid_def.VER_LINUX)
		{
			osName = "Linux";
		}
		else if (osType == rfid_def.VER_WINDOWS)
		{
			osName = "Windows";
		}

		if (arType == rfid_def.AR_X86)
		{
			architecture = "x86";
		}
		else if (arType == rfid_def.AR_X64)
		{
			architecture = "x64";
		}

		if (osName.equals("Windows"))
		{
			libPath = String.format("%s/libs/%s/%s/rfidlib_aip_iso18000p6C.dll",
					sLibPath, osName, architecture);
			System.load(libPath);
			libPath = String.format("%s/libs/%s/%s/jni_rfidlib_aip_iso18000p6C.dll",
					sLibPath, osName, architecture);
			System.load(libPath);
		}
		else if (osName.equals("Linux"))
		{
		}
	}
	
	public native static long ISO18000p6C_CreateInvenParam(
			long hInvenParamSpecList, byte AntennaID, byte Sel, byte Session,
			byte Target, byte Q);

	public native static int ISO18000p6C_SetInvenSelectParam(
			long hIso18000p6CInvenParam, byte target, byte action,
			byte memBank, long dwPointer, byte[] maskBits, int maskBitLen,
			byte truncate);

	public native static int ISO18000p6C_SetInvenMetaDataFlags(
			long hIso18000p6CInvenParam, long flags);

	public native static int ISO18000p6C_SetInvenReadParam(
			long hIso18000p6CInvenParam, byte MemBank, long WordPtr,
			byte WordCount);
	

	public native static int ISO18000p6C_ParseTagReport(long hTagReport,
			Long aip_id, Long tag_id, Long ant_id, Long metaFlags,
			byte[] tagdata, Long tdLen);

	public native static long ISO18000p6C_CreateTAWrite(
			long hIso18000p6CInvenParam, byte memBank, long wordPtr,
			long wordCnt, byte[] pdatas, long nSize);

	public native static int ISO18000p6C_CheckTAWriteResult(long hTagReport);

	public native static int ISO18000p6C_SetInvenAccessPassword(
			long hIso18000p6CInvenParam, long pwd);

	public native static long ISO18000p6C_CreateTALock(
			long hIso18000p6CInvenParam, int mask, int action);

	public native static int ISO18000p6C_CheckTALockResult(long hTagReport);
}
