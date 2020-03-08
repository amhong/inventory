package com.dongduo.library.inventory.service;

import java.util.Vector;

public class Parameters
{
	 public SELECTION m_sel = new SELECTION();
     public META_FLAGS m_metaFlags = new META_FLAGS();
     public long m_accessPwd=0;
     public INVEN_READ m_read = new INVEN_READ();
     public EMBEDDED_WRITE m_write = new EMBEDDED_WRITE();
     public int m_timeout=150;
     public EMBEDDED_Lock m_lock = new EMBEDDED_Lock();
   
     
	public class SELECTION
	{
		public boolean m_enable = false;
		public byte m_target = 0x04;
		public byte m_action = 0x00;
		public byte m_memBank = 0x01;
		public long m_pointer = 0x20;
		public byte m_maskBitsLength;
		public Vector<Byte> m_maskBits = new Vector<Byte>();
	}

	public class INVEN_READ
	{
		public boolean m_enable = false;
		public byte m_memBank = 0x00;
		public long m_wordPtr = 0;
		public long m_wordCnt = 0;
	}

	public class EMBEDDED_WRITE
	{
		public boolean m_enable = false;
		public byte m_memBank = 0x00;
		public long m_wordPtr = 2;
		public long m_wordCnt = 0x01;
		public Vector<Byte> m_datas = new Vector<Byte>();
	}

	public class EMBEDDED_Lock
	{
		public boolean m_enable = false;
		public boolean m_userMemSelected = false;
		public boolean m_TIDMemSelected = false;
		public boolean m_EPCMemSelected = false;
		public boolean m_accessPwdSelected = false;
		public boolean m_killPwdSelected = false;
		public int m_userMem = 0;
		public int m_TIDMem = 0;
		public int m_EPCMem = 0;
		public long m_accessPwd = 0;
		public long m_killPwd = 0;
	}

	public class META_FLAGS
	{
		public boolean m_enable = false;
		public boolean m_EPC = false;
		public boolean m_antennaID = false;
		public boolean m_timestamp = false;
		public boolean m_frequency = false;
		public boolean m_RSSI = false;
		public boolean m_readCnt = false;
		public boolean m_tagData = false;
	}
}
