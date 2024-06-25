CREATE TABLE IF NOT EXISTS  `ecs_info` (
  `id` int(22) NOT NULL AUTO_INCREMENT comment '序号',
  `node`  varchar(128) comment '节点',
  `org_name` varchar(128) comment '组织',
  `rs_name`  varchar(128) comment '资源集',
  `vm_id`  varchar(30) comment '实例ID',
  `vm_ip`  varchar(30) comment 'IP地址',
  `vm_name`  varchar(30) comment '实例名称',
  `vm_cpu`  varchar(30) comment 'Cpu',
  `vm_mem`  varchar(30) comment '内存',
  `vm_os`  varchar(30) comment '系统名称',
  `sys_disk`  varchar(30) comment '系统盘',
  `data_disk`  varchar(30) comment '数据盘',
  `vpc`  varchar(30) comment '网络',
  `create_dt`  timestamp comment '创建时间',
  `update_dt`  timestamp comment '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS  `oss_info` (
  `id` int(22) NOT NULL AUTO_INCREMENT comment '序号',
  `node`  varchar(128) comment '节点',
  `org_name` varchar(128) comment '组织',
  `rs_name`  varchar(128) comment '资源集',
  `bucket_id`  varchar(30) comment '实例ID',
  `quotal`  varchar(30) comment '存储',
  `used`  varchar(30) comment '存储空间',
  `usage`  varchar(30) comment '使用率',
  `create_dt`  timestamp comment '创建时间',
  `update_dt`  timestamp comment '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE IF NOT EXISTS  `kv_info` (
    `id` int(22) NOT NULL AUTO_INCREMENT comment '序号',
    `node`  varchar(128) comment '节点',
    `org_name` varchar(128) comment '组织',
    `rs_name`  varchar(128) comment '资源集',
    `redis_id`  varchar(30) comment '实例ID',
    `create_dt`  timestamp comment '创建时间',
    `update_dt`  timestamp comment '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS  `slb_info` (
    `id` int(22) NOT NULL AUTO_INCREMENT comment '序号',
    `node`  varchar(128) comment '节点',
    `org_name` varchar(128) comment '组织',
    `rs_name`  varchar(128) comment '资源集',
    `slb_id`  varchar(30) comment '实例ID',
    `slb_ip`  varchar(30) comment 'IP地址',
    `slb_name`  varchar(30) comment '实例名称',
    `slb_port`  varchar(30) comment '实例端口',
    `create_dt`  timestamp comment '创建时间',
    `update_dt`  timestamp comment '更新时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8;