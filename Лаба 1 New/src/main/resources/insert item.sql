INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'root', TRUE, NULL, NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'user', TRUE, (SELECT Id FROM Item WHERE Name = 'root'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Desktop', TRUE, (SELECT Id FROM Item WHERE Name = 'user'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Study', TRUE, (SELECT Id FROM Item WHERE Name = 'Desktop'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'CSharp_Lab', TRUE, (SELECT Id FROM Item WHERE Name = 'Study'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'sourcecode.cs', FALSE, (SELECT Id FROM Item WHERE Name = 'CSharp_Lab'), NOW(), NOW(), 1024);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Python_Notes.txt', FALSE, (SELECT Id FROM Item WHERE Name = 'Study'), NOW(), NOW(), 2048);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Algebra_Homework.pdf', FALSE, (SELECT Id FROM Item WHERE Name = 'Study'), NOW(), NOW(), 4096);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'AI_Research.docx', FALSE, (SELECT Id FROM Item WHERE Name = 'Study'), NOW(), NOW(), 3072);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Memes', TRUE, (SELECT Id FROM Item WHERE Name = 'Desktop'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'funny_cat.jpg', FALSE, (SELECT Id FROM Item WHERE Name = 'Memes'), NOW(), NOW(), 512);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'dank_meme.png', FALSE, (SELECT Id FROM Item WHERE Name = 'Memes'), NOW(), NOW(), 1024);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'relatable_comic.gif', FALSE, (SELECT Id FROM Item WHERE Name = 'Memes'), NOW(), NOW(), 2048);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Documents', TRUE, (SELECT Id FROM Item WHERE Name = 'user'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Resume.docx', FALSE, (SELECT Id FROM Item WHERE Name = 'Documents'), NOW(), NOW(), 5120);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Budget.xlsx', FALSE, (SELECT Id FROM Item WHERE Name = 'Documents'), NOW(), NOW(), 4096);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Project_Plan.pptx', FALSE, (SELECT Id FROM Item WHERE Name = 'Documents'), NOW(), NOW(), 8192);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Downloads', TRUE, (SELECT Id FROM Item WHERE Name = 'user'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'installer.exe', FALSE, (SELECT Id FROM Item WHERE Name = 'Downloads'), NOW(), NOW(), 10240);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'free_ebook.pdf', FALSE, (SELECT Id FROM Item WHERE Name = 'Downloads'), NOW(), NOW(), 2048);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'sample_image.png', FALSE, (SELECT Id FROM Item WHERE Name = 'Downloads'), NOW(), NOW(), 4096);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Pictures', TRUE, (SELECT Id FROM Item WHERE Name = 'user'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'vacation_photo.jpg', FALSE, (SELECT Id FROM Item WHERE Name = 'Pictures'), NOW(), NOW(), 5120);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'profile_picture.png', FALSE, (SELECT Id FROM Item WHERE Name = 'Pictures'), NOW(), NOW(), 3072);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'family_album', TRUE, (SELECT Id FROM Item WHERE Name = 'Pictures'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'wedding_photo1.jpg', FALSE, (SELECT Id FROM Item WHERE Name = 'family_album'), NOW(), NOW(), 6144);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'birthday_party.jpg', FALSE, (SELECT Id FROM Item WHERE Name = 'family_album'), NOW(), NOW(), 7168);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Music', TRUE, (SELECT Id FROM Item WHERE Name = 'user'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'favorite_song.mp3', FALSE, (SELECT Id FROM Item WHERE Name = 'Music'), NOW(), NOW(), 4096);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'podcast_episode.m4a', FALSE, (SELECT Id FROM Item WHERE Name = 'Music'), NOW(), NOW(), 8192);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'chill_playlist', TRUE, (SELECT Id FROM Item WHERE Name = 'Music'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'track1.mp3', FALSE, (SELECT Id FROM Item WHERE Name = 'chill_playlist'), NOW(), NOW(), 1024);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'track2.mp3', FALSE, (SELECT Id FROM Item WHERE Name = 'chill_playlist'), NOW(), NOW(), 2048);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Videos', TRUE, (SELECT Id FROM Item WHERE Name = 'user'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'movie.mp4', FALSE, (SELECT Id FROM Item WHERE Name = 'Videos'), NOW(), NOW(), 10240);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'tutorial_video.mkv', FALSE, (SELECT Id FROM Item WHERE Name = 'Videos'), NOW(), NOW(), 8192);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'personal_recording.mov', FALSE, (SELECT Id FROM Item WHERE Name = 'Videos'), NOW(), NOW(), 7168);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Programs', TRUE, (SELECT Id FROM Item WHERE Name = 'root'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Tools', TRUE, (SELECT Id FROM Item WHERE Name = 'Programs'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'word.exe', FALSE, (SELECT Id FROM Item WHERE Name = 'Tools'), NOW(), NOW(), 5120);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'excel.exe', FALSE, (SELECT Id FROM Item WHERE Name = 'Tools'), NOW(), NOW(), 6144);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'powerpoint.exe', FALSE, (SELECT Id FROM Item WHERE Name = 'Tools'), NOW(), NOW(), 8192);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'notepad.exe', FALSE, (SELECT Id FROM Item WHERE Name = 'Tools'), NOW(), NOW(), 2048);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'Game', TRUE, (SELECT Id FROM Item WHERE Name = 'Programs'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'game_launcher.exe', FALSE, (SELECT Id FROM Item WHERE Name = 'Game'), NOW(), NOW(), 10240);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'game_library.dll', FALSE, (SELECT Id FROM Item WHERE Name = 'Game'), NOW(), NOW(), 4096);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'game_asset1.obj', FALSE, (SELECT Id FROM Item WHERE Name = 'Game'), NOW(), NOW(), 2048);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'game_asset2.obj', FALSE, (SELECT Id FROM Item WHERE Name = 'Game'), NOW(), NOW(), 2048);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'DevelopmentTools', TRUE, (SELECT Id FROM Item WHERE Name = 'Programs'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'compiler.exe', FALSE, (SELECT Id FROM Item WHERE Name = 'DevelopmentTools'), NOW(), NOW(), 5120);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'dev_library.dll', FALSE, (SELECT Id FROM Item WHERE Name = 'DevelopmentTools'), NOW(), NOW(), 4096);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'debugger.exe', FALSE, (SELECT Id FROM Item WHERE Name = 'DevelopmentTools'), NOW(), NOW(), 4096);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'System', TRUE, (SELECT Id FROM Item WHERE Name = 'root'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'system_log.txt', FALSE, (SELECT Id FROM Item WHERE Name = 'System'), NOW(), NOW(), 3072);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'drivers', TRUE, (SELECT Id FROM Item WHERE Name = 'System'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'network_driver.sys', FALSE, (SELECT Id FROM Item WHERE Name = 'drivers'), NOW(), NOW(), 4096);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'graphics_driver.sys', FALSE, (SELECT Id FROM Item WHERE Name = 'drivers'), NOW(), NOW(), 5120);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'configs', TRUE, (SELECT Id FROM Item WHERE Name = 'System'), NOW(), NOW(), 0);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'settings.ini', FALSE, (SELECT Id FROM Item WHERE Name = 'configs'), NOW(), NOW(), 2048);
INSERT INTO Item (Id, Name, IsFolder, ParentId, CreatedAt, UpdatedAt, Size) VALUES
    (gen_random_uuid(), 'user_prefs.cfg', FALSE, (SELECT Id FROM Item WHERE Name = 'configs'), NOW(), NOW(), 1024);
