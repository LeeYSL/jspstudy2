package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;

import model.mapper.BoardMapper;

public class BoardMybatsDao {
	private Class<BoardMapper> cls = BoardMapper.class;
	private Map<String,Object> map = new HashMap<>();
	public int maxnum() {
		SqlSession session = MybatisConnection.getConnection();
		try {
		    return session.getMapper(cls).maxnum();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
        	 MybatisConnection.close(session);
		}
		return 0;
	}

	public boolean insert(Board board) {
		 SqlSession session =MybatisConnection.getConnection();
		try {
	
		   return session.getMapper(cls).insert(board) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		     MybatisConnection.close(session);
		}
		return false;
	}

	// 게시판 종류별 등록된 게시물 건수 리턴
	public int boardCount(String boardid, String column, String find) {
		 SqlSession session = MybatisConnection.getConnection();
		try {
			map.clear();
			map.put("boardid", boardid);
		    map.put("column", column);
		    if(column != null) {
				String[] cols = column.split(",");
		    switch(cols.length) {
				case 3 : map.put("col3" , cols[2].trim());
				case 2 : map.put("col2" , cols[1].trim());
				case 1 : map.put("col1" , cols[0].trim());				
			}
				
				map.put("find", find);
			}

			return session.getMapper(cls).boardCount(map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {  
			MybatisConnection.close(session);
		}
		return 0;
	}

	public List<Board> list(String boardid, int pageNum, int limit, String column, String find) {
            SqlSession session = MybatisConnection.getConnection();
		try {
			map.clear();
			map.put("boardid", boardid);
			map.put("start", (pageNum-1) * limit);
			map.put("limit", limit);			
			map.put("column", column);			
			if(column != null) {
				String[] cols = column.split(",");
		    switch(cols.length) {
				case 3 : map.put("col3" , cols[2].trim());
				case 2 : map.put("col2" , cols[1].trim());
				case 1 : map.put("col1" , cols[0].trim());				
			}
				
				map.put("find", find);
			}

		
			return session.getMapper(cls).selectlist(map);
		
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
	          MybatisConnection.close(session);
		}
		return null;
	}

	public Board selectOne(int num) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(cls).selectone(num);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MybatisConnection.close(session);
		}
		return null;

	}

	public void readcntAdd(int num) {
          SqlSession session = MybatisConnection.getConnection();
		try {
	       session.getMapper(cls).readcntAdd(num);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MybatisConnection.close(session);
		}

	}

	public void grpStepAdd(int grp, int grpstep) {
	        SqlSession session = MybatisConnection.getConnection();
		try {
		      session.getMapper(cls).grpStepAdd(grp,grpstep);																																									// 부분
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		     MybatisConnection.close(session);
		}

	}

	public boolean update(Board board) {
	      SqlSession session = MybatisConnection.getConnection();
		try {
		  return session.getMapper(cls).update(board) > 0; 
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			MybatisConnection.close(session);
		}
		return false;

	}

	public boolean delete(int num) {
		SqlSession session = MybatisConnection.getConnection();
		try {
			return session.getMapper(cls).delete(num) > 0;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			MybatisConnection.close(session);
		}
		return false;

	}
}
