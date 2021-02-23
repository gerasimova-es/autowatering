insert into lighting(id, enabled, start_time, stop_time) values (1, true, '08:00:00.000', '23:00:00.000');
insert into vaporizer(id, enabled, min_humidity, check_interval) values (1, false, 70, 2);
insert into whistling(id, enabled, duration, start_time, stop_time) values (1, false, 100, '09:00:00.000', '22:00:00.000');
insert into watering(id, enabled, min_humidity, duration, check_interval) values (1, false, 200, 3, 60);