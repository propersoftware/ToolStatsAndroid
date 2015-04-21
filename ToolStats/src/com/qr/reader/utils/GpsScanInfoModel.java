package com.qr.reader.utils;

import java.io.Serializable;

public class GpsScanInfoModel implements Serializable{
	
	// default constructor, probably not needed....
	public GpsScanInfoModel()
	{
		
	}
	private String UserName = "";
	private String CompanyName = "";
	private String ProjectNo = "";
	private String StreetAddress1 = "";
	private String StreetAddress2 = "";
	private String City = "";
	private String StateProvince = "";
	private String ZipCode = "";
	private String Country = "";
	private Double Lat = 0.0;
	private Double Long = 0.0;
	private Boolean IsManualEntry = true;
	private String Comment = "";
	
	public String getUserName() { return UserName; }
	public String getCompanyName() { return CompanyName; }
	public String getProjectNo() { return ProjectNo; }
	public String getStreetAddress1() { return StreetAddress1; }
	public String getStreetAddress2() { return StreetAddress2; }
	public String getCity() { return City; }
	public String getStateProvince() { return StateProvince; }
	public String getZipCode() { return ZipCode; }
	public String getCountry() { return Country; }
	public String getComment() { return Comment; }
	public Double getLat() { return Lat; }
	public Double getLong() { return Long; }
	public Boolean getIsManualEntry() { return IsManualEntry; }
	
	public void setUserName(String _UserName){ UserName = _UserName; }
	public void setCompanyName(String _CompanyName){ CompanyName = _CompanyName; }
	public void setProjectNo(String _ProjectNo){ ProjectNo = _ProjectNo; }
	public void setStreetAddress1(String _StreetAddress1){ StreetAddress1 = _StreetAddress1; }
	public void setStreetAddress2(String _StreetAddress2){ StreetAddress2 = _StreetAddress2; }
	public void setCity(String _City){ City = _City; }
	public void setStateProvince(String _StateProvince){ StateProvince = _StateProvince; }
	public void setZipCode(String _ZipCode){ ZipCode = _ZipCode; }
	public void setCountry(String _Country){ Country = _Country; }
	public void setComment(String _Comment){ Comment = _Comment; }
	public void setLat(Double _Lat){ Lat = _Lat; }
	public void setLong(Double _Long){ Long = _Long; }
	public void setIsManualEntry(Boolean _IsManualEntry){ IsManualEntry = _IsManualEntry; }
	@Override
	public boolean equals(Object o) {
		GpsScanInfoModel CompareObject = (GpsScanInfoModel)o;
		return CompareObject.getUserName() == this.UserName && CompareObject.getCompanyName() == this.CompanyName
				&& CompareObject.getProjectNo() == this.ProjectNo && CompareObject.getStreetAddress1() == this.StreetAddress1
				&& CompareObject.getStreetAddress2() == this.StreetAddress2 && CompareObject.getCity() == this.City
				&& CompareObject.getStateProvince() == this.StateProvince && CompareObject.getZipCode() == this.ZipCode
				&& CompareObject.getCountry() == this.Country && CompareObject.getComment() == this.Comment;
	}
}
