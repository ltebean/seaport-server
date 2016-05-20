CREATE TABLE `App` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL DEFAULT '',
  `userId` bigint(20) unsigned NOT NULL,
  `secret` varchar(128) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  KEY `secret` (`secret`),
  KEY `userId` (`userId`)
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

CREATE TABLE `User` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `token` varchar(128) DEFAULT NULL,
  `passwordHash` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`),
  UNIQUE KEY `token` (`token`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;

