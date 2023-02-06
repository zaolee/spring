package com.zerock.controller;

import java.sql.Connection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "file:src/main/webapp/WEB-INF/spring/**/*.xml" })
//이 과정이 커넥션 풀을 만드는 과정임 ~~.xml 파일을 참조해서 만들어라

public class DataSourceTest {

	@Inject
	private DataSource ds;

	@Test
	public void testConection() throws Exception {

		try (Connection con = ds.getConnection()) {

			System.out.println(con);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
