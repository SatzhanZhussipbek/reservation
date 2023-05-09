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
    id NVARCHAR(80) NOT NULL,
    description NVARCHAR(80) NOT NULL,
    photo BLOB NOT NULL,
    type NVARCHAR(80) NOT NULL,
    capacity LONG NOT NULL,
    floor LONG NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE if not exists Reserve_item
(
    reservation_id IDENTITY,
    period Object,
    user_id LONG,
    room_id LONG,
    description NVARCHAR(256),
    PRIMARY KEY(reservation_id),
    FOREIGN KEY (user_id) REFERENCES Clients(id),
    FOREIGN KEY (room_id) REFERENCES Rooms(id)
);