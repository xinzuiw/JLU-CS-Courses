package com.ssdult.eflowerShop.dao.impl;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.ssdult.eflowerShop.dao.BaseDao;
import com.ssdult.eflowerShop.dao.FlowerDao;
import com.ssdult.eflowerShop.entity.Flower;

/**
 * FlowerDao针对MySQL数据库的实现类。
 */
public class FlowerDaoImpl extends BaseDao implements FlowerDao{
private Connection conn = null; // 保存数据库连接
private PreparedStatement pstmt = null; // 用于执行SQL语句
private ResultSet rs = null; // 用户保存查询结果集
/**
 * 查询所有鲜花
 */
@Override
public List<Flower> getAllFlower() {
	List<Flower> flowerList = new ArrayList<Flower>();
	try {
	String preparedSql = "select id,name,typeName,owner_id,store_id,price from flower ";
	conn = getConn(); // 得到数据库连接
	pstmt = conn.prepareStatement(preparedSql); // 得到PreparedStatement对象
	rs = pstmt.executeQuery(); // 执行SQL语句
while (rs.next()) {
Flower flower = new Flower();
	  flower.setId(rs.getInt(1));
	  flower.setName(rs.getString(2));
	  flower.setTypeName(rs.getString(3));
	  flower.setOwnerId(rs.getInt(4));
	  flower.setStoreId(rs.getInt(5));
	  flower.setPrice(rs.getDouble(6));
	  flowerList.add(flower);
		 }
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			super.closeAll(conn, pstmt, rs);
		}
		return flowerList;
	}
/**
 * 根据参数查询相应符合条件鲜花
 */
	@Override
	public List<Flower> selectFlower(String sql, String[] param) {
		List<Flower> flowerList = new ArrayList<Flower>();
		try {
		conn = getConn(); // 得到数据库连接
		pstmt = conn.prepareStatement(sql); // 得到PreparedStatement对象
		if (param != null) {
			for (int i = 0; i < param.length; i++) {
				pstmt.setString(i + 1, param[i]); // 为预编译sql设置参数
			}
		}
		rs = pstmt.executeQuery(); // 执行SQL语句
		while (rs.next()) {
		  Flower flower = new Flower();
		  flower.setId(rs.getInt(1));
		  flower.setName(rs.getString(2));
		  flower.setTypeName(rs.getString(3));
		  flower.setOwnerId(rs.getInt(4));
		  flower.setStoreId(rs.getInt(5));
		  flower.setPrice(rs.getDouble(6));
		  flowerList.add(flower);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			super.closeAll(conn, pstmt, rs);
		}
		return flowerList;
	}
	/**
	 * 更新鲜花信息
	 */
	@Override
	public int updateFlower(String sql, Object[] param) {
		int count = super.executeSQL(sql, param);
		return count;
	}
}	
//省略FlowerDao的其他方法
}
