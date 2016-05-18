CREATE TABLE `App` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `secret` varchar(128) NOT NULL DEFAULT '',
  `bucket` varchar(255) NOT NULL DEFAULT '',
  `baseUrl` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `secret` (`secret`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `Package` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `appId` bigint(20) unsigned NOT NULL,
  `name` varchar(255) NOT NULL DEFAULT '',
  `version` varchar(255) NOT NULL DEFAULT '',
  `url` varchar(512) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `appId` (`appId`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;