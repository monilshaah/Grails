dataSource {
    pooled = true
    jmxExport = true
    driverClassName = "org.h2.Driver"
    username = "sa"
    password = ""
}
hibernate {
    cache.use_second_level_cache = true
    cache.use_query_cache = false
//    cache.region.factory_class = 'net.sf.ehcache.hibernate.EhCacheRegionFactory' // Hibernate 3
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory' // Hibernate 4
    singleSession = true // configure OSIV singleSession mode
    flush.mode = 'manual' // OSIV session flush mode outside of transactional context
}

// environment specific settings
environments {	
	development {
		dataSource {
			dbCreate = "update"  // 'create', 'create-drop','update'
			url = "jdbc:mysql://localhost:3306/grails_demo"
			driverClassName = "com.mysql.jdbc.Driver"
			username = "root"
			password = ""
		}
	}
	test {
		dataSource {
			dbCreate = "update"  // 'create', 'create-drop','update'
			url = "jdbc:mysql://localhost:3306/cmpe281"
			driverClassName = "com.mysql.jdbc.Driver"
			username = "root"
			password = ""
		}
	}
	production {
		dataSource {
			dbCreate = "update"  // 'create', 'create-drop','update'
			url = "jdbc:mysql://b885b05b20c6ba:56b4eee1@us-cdbr-iron-east-01.cleardb.net:3306/ad_e5d8c795311760c"
			driverClassName = "com.mysql.jdbc.Driver"
			username = "b885b05b20c6ba"
			password = "56b4eee1"
		}
	}		
}
