-- 启动时初始化几篇示例文章
INSERT INTO ts_article (id, title, language, category, content, word_count, difficulty, sort, create_date, status) VALUES
('art-zh-1', '练习打字', 'zh', '通用', '熟练的打字是一项非常实用的技能。无论是写文章还是写代码，稳定而准确的输入都能让我们把注意力放在思考本身。', 48, '1', 10, CURRENT_TIMESTAMP, '0'),
('art-zh-2', '关于学习', 'zh', '通用', '学习是一个不断积累的过程。今天比昨天进步一点，明天又比今天多懂一些，时间久了就会有明显的变化。', 45, '2', 20, CURRENT_TIMESTAMP, '0'),
('art-en-1', 'The Quick Fox', 'en', 'general', 'The quick brown fox jumps over the lazy dog. Practice these sentences and your fingers will learn where every key lives.', 21, '1', 30, CURRENT_TIMESTAMP, '0');

INSERT INTO ts_competition (id, title, article_id, description, comp_status, create_date, status) VALUES
('comp-1', '新手中文赛', 'art-zh-1', '面向新手的中文打字比赛示例', '1', CURRENT_TIMESTAMP, '0');
