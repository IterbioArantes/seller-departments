package mode.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{
	
	private Connection conn;
	
	public DepartmentDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public void insert(Department obj) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = conn.prepareStatement("INSERT INTO department "
									 + "(Name) "
									 + "VALUES (?)", Statement.RETURN_GENERATED_KEYS);
			
			ps.setString(1,obj.getName());
			
			int rows = ps.executeUpdate();
			
			if(rows > 0) {
				rs = ps.getGeneratedKeys();
				
				if(rs.next()) {
					obj.setId(rs.getInt(1));
				}	
			}else {
				throw new DbException("No rows affected.");
			}
			
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public void update(Department obj) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = conn.prepareStatement("UPDATE department "
									 + "SET Name = ? "
									 + "WHERE Id = ?");
			
			ps.setString(1,obj.getName());
			ps.setInt(2,obj.getId());
			
			int rows = ps.executeUpdate();
			
			if(rows == 0) {
				throw new DbException("Department id not found to update.");
				}	
			
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public void deleteById(Integer id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = conn.prepareStatement("DELETE from department "
									 + "WHERE Id = ?");
			
			ps.setInt(1,id);
			
			int rows = ps.executeUpdate();
			
			if(rows == 0) {
				throw new DbException("Department Id not found to delete.");
				}	
			
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
		
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			ps = conn.prepareStatement("SELECT * from department "
									 + "WHERE Id = ?");
			
			ps.setInt(1,id);
			
			rs = ps.executeQuery();
			
			if(rs.next()) {
				return new Department(rs.getInt("Id"),rs.getString("Name"));
			}
			return null;
			
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

	@Override
	public List<Department> findAll() {
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<Department> listDepartment = new ArrayList<Department>();
		
		try {
			
			ps = conn.prepareStatement("SELECT * from department "
									 + "ORDER BY Name");
			
			rs = ps.executeQuery();
			
			while(rs.next()) {
				listDepartment.add(new Department(rs.getInt("Id"),rs.getString("Name")));				
			}
			
			return listDepartment;
			
		}catch (SQLException e){
			throw new DbException(e.getMessage());
		}finally {
			DB.closeStatement(ps);
			DB.closeResultSet(rs);
		}
	}

}
