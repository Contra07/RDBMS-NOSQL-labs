-- Связь процесса TextEditorProcess с файлом word.exe
INSERT INTO ProcessFile (Id, ProcessId, ItemId, Access) VALUES
    (gen_random_uuid(), 
     (SELECT Id FROM Process WHERE Name = 'TextEditorProcess'), 
     (SELECT Id FROM Item WHERE Name = 'word.exe'), 'Read');

-- Связь процесса ExcelProcess с файлом excel.exe
INSERT INTO ProcessFile (Id, ProcessId, ItemId, Access) VALUES
    (gen_random_uuid(), 
     (SELECT Id FROM Process WHERE Name = 'ExcelProcess'), 
     (SELECT Id FROM Item WHERE Name = 'excel.exe'), 'Read');

-- Связь процесса PowerPointProcess с файлом powerpoint.exe
INSERT INTO ProcessFile (Id, ProcessId, ItemId, Access) VALUES
    (gen_random_uuid(), 
     (SELECT Id FROM Process WHERE Name = 'PowerPointProcess'), 
     (SELECT Id FROM Item WHERE Name = 'powerpoint.exe'), 'Read');

-- Связь процесса GameLauncherProcess с файлом game_launcher.exe
INSERT INTO ProcessFile (Id, ProcessId, ItemId, Access) VALUES
    (gen_random_uuid(), 
     (SELECT Id FROM Process WHERE Name = 'GameLauncherProcess'), 
     (SELECT Id FROM Item WHERE Name = 'game_launcher.exe'), 'Read');

-- Связь процесса CompilerProcess с файлом compiler.exe
INSERT INTO ProcessFile (Id, ProcessId, ItemId, Access) VALUES
    (gen_random_uuid(), 
     (SELECT Id FROM Process WHERE Name = 'CompilerProcess'), 
     (SELECT Id FROM Item WHERE Name = 'compiler.exe'), 'Read');

-- Связь процесса DebuggerProcess с файлом debugger.exe
INSERT INTO ProcessFile (Id, ProcessId, ItemId, Access) VALUES
    (gen_random_uuid(), 
     (SELECT Id FROM Process WHERE Name = 'DebuggerProcess'), 
     (SELECT Id FROM Item WHERE Name = 'debugger.exe'), 'Read');

-- Связь процесса ImageProcessor с файлом profile_picture.png
INSERT INTO ProcessFile (Id, ProcessId, ItemId, Access) VALUES
    (gen_random_uuid(), 
     (SELECT Id FROM Process WHERE Name = 'ImageProcessor'), 
     (SELECT Id FROM Item WHERE Name = 'profile_picture.png'), 'Write');

-- Связь процесса MusicProcessor с файлом track1.mp3
INSERT INTO ProcessFile (Id, ProcessId, ItemId, Access) VALUES
    (gen_random_uuid(), 
     (SELECT Id FROM Process WHERE Name = 'MusicProcessor'), 
     (SELECT Id FROM Item WHERE Name = 'track1.mp3'), 'Read');

-- Связь процесса VideoProcessor с файлом tutorial_video.mkv
INSERT INTO ProcessFile (Id, ProcessId, ItemId, Access) VALUES
    (gen_random_uuid(), 
     (SELECT Id FROM Process WHERE Name = 'VideoProcessor'), 
     (SELECT Id FROM Item WHERE Name = 'tutorial_video.mkv'), 'Read');

-- Связь процесса DocumentProcessor с файлом Resume.docx
INSERT INTO ProcessFile (Id, ProcessId, ItemId, Access) VALUES
    (gen_random_uuid(), 
     (SELECT Id FROM Process WHERE Name = 'DocumentProcessor'), 
     (SELECT Id FROM Item WHERE Name = 'Resume.docx'), 'Read');
