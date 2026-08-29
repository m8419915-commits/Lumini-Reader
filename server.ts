import express from 'express';
import path from 'path';
import { createServer as createViteServer } from 'vite';
import { GoogleGenAI } from '@google/genai';

const app = express();
const PORT = 3000;

app.use(express.json());

// Lazy-initialized Gemini client
let genAiClient: GoogleGenAI | null = null;
function getGeminiClient(): GoogleGenAI | null {
  const apiKey = process.env.GEMINI_API_KEY;
  if (!apiKey) return null;
  if (!genAiClient) {
    genAiClient = new GoogleGenAI({ apiKey });
  }
  return genAiClient;
}

// Health check endpoint
app.get('/api/health', (req, res) => {
  res.json({ status: 'ok', service: 'Lumina Reader Core API' });
});

// AI Assistant endpoint
app.post('/api/ai/chat', async (req, res) => {
  try {
    const { message, context } = req.body;
    const client = getGeminiClient();

    if (client) {
      const systemInstruction = `You are Lumina AI, the built-in intelligent manga co-pilot and narrative analyst for Lumina Reader.
You provide deep lore insights, chapter summaries, character power scaling comparisons, narrative arc timeline explanations, and personalized manga recommendations.
Keep answers concise, engaging, and rich with manga culture references. Mention matching genres and themes.`;

      const response = await client.models.generateContent({
        model: 'gemini-2.5-flash',
        contents: [
          { role: 'user', parts: [{ text: `${systemInstruction}\n\nContext: ${JSON.stringify(context || {})}\nUser Query: ${message}` }] }
        ]
      });

      const responseText = response.text || 'I analyzed your request in the Lumina knowledge base.';
      return res.json({
        reply: responseText,
        recommendations: [
          {
            id: 'rec_1',
            title: 'Chainsaw Man',
            coverUrl: 'https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80',
            tag1: 'Dark Fantasy',
            tag2: 'Action',
            description: 'Intense dark urban fantasy with supernatural devil pacts and rapid pacing.',
            matchScore: 98
          },
          {
            id: 'rec_2',
            title: 'Jujutsu Kaisen',
            coverUrl: 'https://images.unsplash.com/photo-1534447677768-be436bb09401?w=600&auto=format&fit=crop&q=80',
            tag1: 'Supernatural',
            tag2: 'Shonen',
            description: 'Complex power systems, cursed energy mechanics, and high-stakes domain clashes.',
            matchScore: 94
          }
        ]
      });
    } else {
      // Intelligent fallback responses when GEMINI_API_KEY is not configured
      const lower = (message || '').toLowerCase();
      let reply = "I've analyzed your reading history and preferences across the Lumina Universe.";
      let recommendations = [
        {
          id: 'rec_1',
          title: 'Chainsaw Man',
          coverUrl: 'https://images.unsplash.com/photo-1578632767115-351597cf2477?w=600&auto=format&fit=crop&q=80',
          tag1: 'Dark Fantasy',
          tag2: 'Supernatural',
          description: 'High-octane action with visceral art and unpredictable plot twists.',
          matchScore: 98
        },
        {
          id: 'rec_2',
          title: 'Solo Leveling',
          coverUrl: 'https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80',
          tag1: 'Webtoon',
          tag2: 'Action RPG',
          description: 'Shadow Monarch progression with jaw-dropping vertical color battle spreads.',
          matchScore: 95
        }
      ];

      if (lower.includes('chapter') || lower.includes('summary')) {
        reply = '📖 **Chapter Breakdown**: The current arc explores the clash between Soul Society and the Quincy Vandenreich. High spiritual pressure awakens dormant abilities while uncovering hidden genealogy.';
      } else if (lower.includes('character') || lower.includes('ichigo') || lower.includes('power')) {
        reply = '👤 **Character Profile**: Ichigo Kurosaki possesses a unique hybrid genome combining Shinigami, Hollow, and Quincy ancestry. In the Universe Map, his spiritual resonance ties directly to the Soul King core.';
      } else if (lower.includes('genre') || lower.includes('recommend') || lower.includes('discovery')) {
        reply = '🧭 **Curated Recommendations**: Based on your high affinity for dark supernatural battle shonen with intricate power systems, these titles are optimal matches:';
      } else {
        reply = `✨ **Lumina AI Analysis**: I found 3 connected lore threads in your current reading queue. Your reading velocity is averaging 3.2 PPM with a 5-day active flame streak.`;
      }

      return res.json({ reply, recommendations });
    }
  } catch (error: any) {
    console.error('Error in /api/ai/chat:', error);
    return res.status(500).json({ error: 'AI processing error', message: error.message });
  }
});

// Extension repository mock/proxy endpoint
app.get('/api/extensions/repo', (req, res) => {
  res.json({
    status: 'success',
    timestamp: new Date().toISOString(),
    packagesCount: 6,
    packages: [
      {
        name: 'MangaDex',
        pkg: 'eu.kanade.tachiyomi.extension.all.mangadex',
        versionName: '1.4.218',
        versionCode: 218,
        lang: 'all',
        apk: 'mangadex-all-v1.4.218.apk',
        icon: 'https://mangadex.org/favicon.ico'
      },
      {
        name: 'Asura Scans',
        pkg: 'eu.kanade.tachiyomi.extension.en.asurascans',
        versionName: '1.4.52',
        versionCode: 52,
        lang: 'en',
        apk: 'asurascans-en-v1.4.52.apk',
        icon: 'https://asuracomic.net/favicon.ico'
      },
      {
        name: 'Flame Comics',
        pkg: 'eu.kanade.tachiyomi.extension.en.flamecomics',
        versionName: '1.3.12',
        versionCode: 12,
        lang: 'en',
        apk: 'flamecomics-en-v1.3.12.apk',
        icon: 'https://flamecomics.xyz/favicon.ico'
      },
      {
        name: 'Webtoons',
        pkg: 'eu.kanade.tachiyomi.extension.en.webtoons',
        versionName: '1.3.4',
        versionCode: 4,
        lang: 'en',
        apk: 'webtoons-en-v1.3.4.apk',
        icon: 'https://www.webtoons.com/favicon.ico'
      },
      {
        name: 'Rawkuma',
        pkg: 'eu.kanade.tachiyomi.extension.ja.rawkuma',
        versionName: '1.2.1',
        versionCode: 1,
        lang: 'ja',
        apk: 'rawkuma-ja-v1.2.1.apk',
        icon: 'https://rawkuma.com/favicon.ico'
      },
      {
        name: 'Manhwa18',
        pkg: 'eu.kanade.tachiyomi.extension.ko.manhwa18',
        versionName: '1.0.8',
        versionCode: 8,
        lang: 'ko',
        apk: 'manhwa18-ko-v1.0.8.apk',
        icon: 'https://manhwa18.com/favicon.ico'
      }
    ]
  });
});

async function startServer() {
  if (process.env.NODE_ENV !== 'production') {
    const vite = await createViteServer({
      server: { middlewareMode: true, host: '0.0.0.0', port: PORT },
      appType: 'spa',
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.join(process.cwd(), 'dist');
    app.use(express.static(distPath));
    app.get('*', (req, res) => {
      res.sendFile(path.join(distPath, 'index.html'));
    });
  }

  app.listen(PORT, '0.0.0.0', () => {
    console.log(`Lumina Reader server listening at http://0.0.0.0:${PORT}`);
  });
}

startServer();
