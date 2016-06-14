package com.mokabuu.bean;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import org.apache.commons.dbutils.DbUtils;

@Named
@RequestScoped
@ManagedBean(name = "bean")
public class bean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -748662861575520550L;

	public String bandname1;
	public String bandname2;
	public String bandname3;
	public String bandname4;

	public String app1;
	public String app2;
	public String app3;
	public String app4;
	
	FacesContext context = FacesContext.getCurrentInstance();

	public String getBandname1() {
		if(bandname1 == null){
			bandname1 = "空";
		}
		return bandname1;
	}

	public void setBandname1(String bandname1) {
		this.bandname1 = bandname1;
	}

	public String getBandname2() {
		if(bandname2 == null){
			bandname2 = "空";
		}
		return bandname2;
	}

	public void setBandname2(String bandname2) {
		this.bandname2 = bandname2;
	}

	public String getBandname3() {
		if(bandname3 == null){
			bandname3 = "空";
		}
		return bandname3;
	}

	public void setBandname3(String bandname3) {
		this.bandname3 = bandname3;
	}

	public String getBandname4() {
		if(bandname4 == null){
			bandname4 = "空";
		}
		return bandname4;
	}

	public void setBandname4(String bandname4) {
		this.bandname4 = bandname4;
	}

	public String getApp1() {
		return app1;
	}

	public void setApp1(String app1) {
		this.app1 = app1;
	}

	public String getApp2() {
		return app2;
	}

	public void setApp2(String app2) {
		this.app2 = app2;
	}

	public String getApp3() {
		return app3;
	}

	public void setApp3(String app3) {
		this.app3 = app3;
	}

	public String getApp4() {
		return app4;
	}

	public void setApp4(String app4) {
		this.app4 = app4;
	}

	private Connection connect() throws SQLException {
		Connection con = null;
		String url = "jdbc:mysql://localhost/my_db";
		String user = "";
		String password = "";

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			con = DriverManager.getConnection(url, user, password);
			con.setAutoCommit(false);
		} catch (ClassNotFoundException e) {
			System.out.println("ClassNotFoundException:" + e.getMessage());
		} catch (SQLException e) {
			System.out.println("SQLException:" + e.getMessage());
		} catch (Exception e) {
			System.out.println("Exception:" + e.getMessage());
		}
		return con;
	}

	public void submit1() {
		appFeedback(app1, 1);
	}

	public void submit2() {
		appFeedback(app2, 2);
	}

	public void submit3() {
		appFeedback(app3, 3);
	}

	public void submit4() {
		appFeedback(app4, 4);
	}
	
	private void appFeedback(String appname, int timeline) {
		if (validate(timeline)) {
			context.addMessage(null, new FacesMessage("【失敗】他のバンドの予約が入っています"));
		} else if(appname == null){
			context.addMessage(null, new FacesMessage("【失敗】バンド名を入力してください"));
		}else {
			Connection con = null;
			PreparedStatement ps = null;

			try {
				con = connect();
				ps = con.prepareStatement("INSERT INTO fantasiafb (bandname, timeline) values (?, ?)");
				ps.setString(1, appname);
				ps.setInt(2, timeline);
				ps.executeUpdate();
				con.commit();
				context.addMessage(null, new FacesMessage("【成功】" + appname + "さんのFBを受け付けました。"));
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DbUtils.closeQuietly(con, ps, null);
			}			
		}
		init();
	}

	private boolean validate(int timeline) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = connect();
			ps = con.prepareStatement("select * from fantasiafb where timeline = ?");
			ps.setInt(1, timeline);
			rs = ps.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(con, ps, rs);
		}
		return false;
	}
	
	public void init() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			con = connect();
			ps = con.prepareStatement("select * from fantasiafb order by timeline");
			rs = ps.executeQuery();
			while(rs.next()){
				switch(rs.getInt("timeline")){
				case 1:
					bandname1 = rs.getString("bandname");
					break;
				case 2:
					bandname2 = rs.getString("bandname");
					break;
				case 3:
					bandname3 = rs.getString("bandname");
					break;
				case 4:
					bandname4 = rs.getString("bandname");
					break;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DbUtils.closeQuietly(con, ps, rs);
		}
	}
}
