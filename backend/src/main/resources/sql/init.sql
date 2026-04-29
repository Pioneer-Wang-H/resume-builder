-- Resume Builder Database Schema

CREATE DATABASE IF NOT EXISTS resume_builder DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE resume_builder;

-- 用户表
DROP TABLE IF EXISTS `user_file`;
DROP TABLE IF EXISTS `job_application`;
DROP TABLE IF EXISTS `resume_section_item`;
DROP TABLE IF EXISTS `resume_section`;
DROP TABLE IF EXISTS `resume`;
DROP TABLE IF EXISTS `resume_template`;
DROP TABLE IF EXISTS `self_evaluation`;
DROP TABLE IF EXISTS `certificate`;
DROP TABLE IF EXISTS `skill_tag`;
DROP TABLE IF EXISTS `work_experience`;
DROP TABLE IF EXISTS `project_experience`;
DROP TABLE IF EXISTS `education`;
DROP TABLE IF EXISTS `basic_info`;
DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `username` VARCHAR(50) NOT NULL UNIQUE,
  `password` VARCHAR(255) NOT NULL,
  `nickname` VARCHAR(50),
  `email` VARCHAR(100),
  `avatar_url` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 基本信息（素材池）
CREATE TABLE `basic_info` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL UNIQUE,
  `name` VARCHAR(50),
  `phone` VARCHAR(20),
  `email` VARCHAR(100),
  `website` VARCHAR(255),
  `linkedin` VARCHAR(255),
  `github` VARCHAR(255),
  `intended_position` VARCHAR(100),
  `city` VARCHAR(50),
  `birth_date` DATE,
  `avatar_url` VARCHAR(255),
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='基本信息';

-- 教育经历（素材池）
CREATE TABLE `education` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `school` VARCHAR(100) NOT NULL,
  `major` VARCHAR(100),
  `degree` VARCHAR(20),
  `gpa` VARCHAR(10),
  `start_date` DATE,
  `end_date` DATE,
  `description` TEXT,
  `sort_order` INT DEFAULT 0,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教育经历';

-- 项目经历（素材池）
CREATE TABLE `project_experience` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `project_name` VARCHAR(100) NOT NULL,
  `role` VARCHAR(50),
  `start_date` DATE,
  `end_date` DATE,
  `url` VARCHAR(255),
  `description` TEXT,
  `highlights` JSON,
  `sort_order` INT DEFAULT 0,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='项目经历';

-- 实习/工作经历（素材池）
CREATE TABLE `work_experience` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `company` VARCHAR(100) NOT NULL,
  `position` VARCHAR(100),
  `start_date` DATE,
  `end_date` DATE,
  `description` TEXT,
  `highlights` JSON,
  `sort_order` INT DEFAULT 0,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='工作经历';

-- 技能标签（素材池）
CREATE TABLE `skill_tag` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `skill_name` VARCHAR(50) NOT NULL,
  `proficiency` INT DEFAULT 3,
  `category` VARCHAR(30),
  `sort_order` INT DEFAULT 0,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='技能标签';

-- 证书奖项（素材池）
CREATE TABLE `certificate` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `issuing_authority` VARCHAR(100),
  `obtain_date` DATE,
  `description` VARCHAR(255),
  `sort_order` INT DEFAULT 0,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='证书奖项';

-- 自我评价（素材池）
CREATE TABLE `self_evaluation` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL UNIQUE,
  `content` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='自我评价';

-- 简历模板
CREATE TABLE `resume_template` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `name` VARCHAR(50) NOT NULL,
  `thumbnail` VARCHAR(255),
  `template_file` VARCHAR(255) NOT NULL,
  `is_builtin` TINYINT DEFAULT 0,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历模板';

-- 简历
CREATE TABLE `resume` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `template_id` BIGINT DEFAULT 1,
  `title` VARCHAR(100) NOT NULL,
  `is_default` TINYINT DEFAULT 0,
  `status` TINYINT DEFAULT 1,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`template_id`) REFERENCES `resume_template`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历';

-- 简历-模块关联
CREATE TABLE `resume_section` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `resume_id` BIGINT NOT NULL,
  `section_type` VARCHAR(30) NOT NULL,
  `enabled` TINYINT DEFAULT 1,
  `sort_order` INT DEFAULT 0,
  FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE CASCADE,
  UNIQUE KEY `uk_resume_section` (`resume_id`, `section_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历模块';

-- 简历-素材关联（勾选了哪些具体经历）
CREATE TABLE `resume_section_item` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `section_id` BIGINT NOT NULL,
  `item_type` VARCHAR(30) NOT NULL,
  `item_id` BIGINT NOT NULL,
  `sort_order` INT DEFAULT 0,
  FOREIGN KEY (`section_id`) REFERENCES `resume_section`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='简历素材关联';

-- 投递记录
CREATE TABLE `job_application` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `resume_id` BIGINT,
  `company` VARCHAR(100) NOT NULL,
  `position` VARCHAR(100),
  `salary_range` VARCHAR(50),
  `location` VARCHAR(50),
  `channel` VARCHAR(50),
  `status` VARCHAR(20) DEFAULT '已投递',
  `apply_date` DATE,
  `notes` TEXT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
  FOREIGN KEY (`resume_id`) REFERENCES `resume`(`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='投递记录';

-- 用户文件
CREATE TABLE `user_file` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `file_name` VARCHAR(255) NOT NULL,
  `file_path` VARCHAR(255) NOT NULL,
  `file_type` VARCHAR(30),
  `file_size` BIGINT,
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户文件';

-- 插入内置模板
INSERT INTO `resume_template` (`name`, `template_file`, `is_builtin`) VALUES
('经典两栏', 'template-classic.html', 1),
('简约线条', 'template-minimal.html', 1),
('左右分栏', 'template-split.html', 1);

-- 操作日志
CREATE TABLE `operation_log` (
  `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
  `user_id` BIGINT NOT NULL,
  `module` VARCHAR(50) NOT NULL COMMENT '操作模块',
  `operation` VARCHAR(100) NOT NULL COMMENT '操作描述',
  `params` TEXT COMMENT '请求参数JSON',
  `ip` VARCHAR(50) COMMENT '请求IP',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  INDEX `idx_user_id` (`user_id`),
  INDEX `idx_module` (`module`),
  INDEX `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志';

-- 插入测试用户 (密码: 123456)
INSERT INTO `user` (`username`, `password`, `nickname`, `email`) VALUES
('test', '$2b$10$/rU2KmVmg2To2Zd98/EhcO8rYwktGgCrAqk71uPWX0ysvMNIMlgC2', '测试用户', 'test@example.com');
