package com.rgretail.grocermax.bean;

import java.util.Date;

public class DateObject implements Comparable<DateObject>{

	private Date dateTime;

	  public Date getDateTime() {
	    return dateTime;
	  }

	  public void setDateTime(Date datetime) {
	    this.dateTime = datetime;
	  }

	  @Override
	  public int compareTo(DateObject o) {
	    return getDateTime().compareTo(o.getDateTime());
	  }
}
