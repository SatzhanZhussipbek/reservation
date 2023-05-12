
CREATE TABLE if not exists Clients
(
    id IDENTITY,
    name NVARCHAR(80) NOT NULL,
    surname NVARCHAR(80) NOT NULL,
    email NVARCHAR(80) NOT NULL,
    password NVARCHAR(256) NOT NULL,
    role VARCHAR(256) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE if not exists Rooms
(
    id NVARCHAR(80),
    description NVARCHAR(80),
    type NVARCHAR(80),
    capacity LONG,
    floor LONG,
    PRIMARY KEY(id)
);

CREATE TABLE if not exists Photo_items
(
    id IDENTITY,
    room_id LONG,
    name NVARCHAR(256),
    PRIMARY KEY(id),
    FOREIGN KEY(room_id) references Rooms(id)
);

CREATE TABLE if not exists Reserve_item
(
    id IDENTITY,
    period Object,
    user_id LONG,
    description NVARCHAR(256),
    room_id LONG,
    PRIMARY KEY(id),
    FOREIGN KEY (user_id) REFERENCES Clients(id)
);