/* Generated by Together */

/*
 * BJAF - Beetle J2EE Application Framework
 * 甲壳虫J2EE企业应用开发框架
 * 版权所有2003-2015 余浩东 (www.beetlesoft.net)
 * 
 * 这是一个免费开源的软件，您必须在
 *<http://www.apache.org/licenses/LICENSE-2.0>
 *协议下合法使用、修改或重新发布。
 *
 * 感谢您使用、推广本框架，若有建议或问题，欢迎您和我联系。
 * 邮件： <yuhaodong@gmail.com/>.
 */
package com.beetle.framework.persistence.access.base;

import com.beetle.framework.persistence.access.operator.SqlParameter;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

 class BatchManerImp implements IAccessManner {
	private String sql;

	private ArrayList<List<SqlParameter>> batchValues;

	public BatchManerImp(String sql, ArrayList<List<SqlParameter>> batchValues) {
		this.sql = sql;
		this.batchValues = batchValues;
	}

	public PreparedStatement accessByPreStatement(Connection conn)
			throws SQLException {
		PreparedStatement ps = conn.prepareStatement(sql);
		for (int j = 0; j < batchValues.size(); j++) {
			List<SqlParameter> rowParameters = batchValues.get(j);
			for (int i = 0; i < rowParameters.size(); i++) {
				SqlParameter sqlParam = rowParameters.get(i);
				if (sqlParam.getValue() == null) {
					ps.setNull(i + 1, sqlParam.getType());
				} else {
					if (sqlParam.getType() == -1001) {
						ps.setObject(i + 1, sqlParam.getValue());
					} else {
						switch (sqlParam.getType()) {
						case Types.VARCHAR:
							ps.setString(i + 1, (String) sqlParam.getValue());
							break;
						case Types.TIMESTAMP:
							ps.setTimestamp(i + 1,
									(Timestamp) sqlParam.getValue());
							break;
						default:
							ps.setObject(i + 1, sqlParam.getValue(),
									sqlParam.getType());
							break;
						}
					}
				}
			}
			ps.addBatch();
			if (!rowParameters.isEmpty()) {
				rowParameters.clear();
			}
		}
		if (!batchValues.isEmpty()) {
			batchValues.clear();
		}
		return ps;
	}

	public CallableStatement accessByCallableStatement(Connection conn)
			throws SQLException {
		throw new com.beetle.framework.AppRuntimeException("not implemented!");
	}
}
