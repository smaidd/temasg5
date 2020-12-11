CREATE TABLE `contacts` (
  `id` char(36) NOT NULL,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `rooms` (
  `id` char(36) NOT NULL,
  `name` varchar(100) NOT NULL,
  `capacity` smallint unsigned NOT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `meetings` (
  `id` char(36) NOT NULL,
  `subject` varchar(100) NOT NULL,
  `description` varchar(500),
  `start` datetime NOT NULL,
  `end` datetime NOT NULL,
  `room_id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `meetings_FK_rooms` (`room_id`),
  CONSTRAINT `meetings_FK_rooms` FOREIGN KEY (`room_id`) REFERENCES `rooms` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `attendees` (
  `id` char(36) NOT NULL,
  `meeting_id` char(36) NOT NULL,
  `contact_id` char(36) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `attendees_FK_meetings` (`meeting_id`),
  KEY `attendees_FK_contacts` (`contact_id`),
  CONSTRAINT `attendees_FK_contacts` FOREIGN KEY (`contact_id`) REFERENCES `contacts` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `attendees_FK_meetings` FOREIGN KEY (`meeting_id`) REFERENCES `meetings` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO contacts (id,first_name,last_name,email) VALUES
	 ('0225bd81-179d-4da3-8b23-42c9727c7bf5','Vasile','Ionescu','test1@gmail.com'),
	 ('313c2aa3-e64a-4597-8be6-84080f5bf8dc','Paul','Mihalache','test2@gmail.com'),
	 ('42790856-6e9e-4dcb-9b76-2fc6c3f59be0','Dan','Popescu','pop@yahoo.com');
	 
INSERT INTO rooms (id,name,capacity) VALUES
	 ('09676279-b8bd-46da-9a04-433d591738f4','Everest', 500),
	 ('0a193667-6910-4fb2-9163-7b9b3d1213e6','Kilimanjaro',100),
	 ('7b93dd7b-2877-43c8-8769-01dd9048c77c','Moldoveanu',500);	 