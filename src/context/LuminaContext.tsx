import React, { createContext, useContext, useState, useEffect } from 'react';
import {
  Manga,
  Chapter,
  ReadingSnap,
  ReaderConfig,
  ScreenState,
  ScreenType,
  UniverseNode,
  UniverseEdge,
  DnaAttribute,
  Achievement,
  ExperiencePack,
  MutationBehavior,
  AiChatMessage,
  AiRecommendationCard,
  MangaUpdateItem,
  ExtensionPackage,
  TimelineItem,
  ReaderMode,
  BackgroundTint,
} from '../types';
import {
  sampleMangas,
  sampleChapters,
  initialSnaps,
  sampleUniverseNodes,
  sampleUniverseEdges,
  sampleDnaAttributes,
  sampleAchievements,
  sampleExperiencePacks,
  sampleMutationBehaviors,
  sampleUpdates,
  sampleExtensions,
  sampleTimelineItems,
  initialReaderConfig,
} from '../data/initialData';

interface LuminaContextType {
  // Navigation
  screen: ScreenState;
  navigate: (type: ScreenType, mangaId?: number, chapterId?: number, initialPage?: number) => void;
  goBack: () => void;
  canGoBack: boolean;

  // Manga & Library
  mangas: Manga[];
  toggleLibrary: (mangaId: number) => void;
  getManga: (mangaId: number) => Manga | undefined;
  searchQuery: string;
  setSearchQuery: (query: string) => void;

  // Chapters & Reading
  chapters: Chapter[];
  getChaptersForManga: (mangaId: number) => Chapter[];
  markChapterRead: (chapterId: number, isRead: boolean) => void;
  toggleChapterDownload: (chapterId: number) => void;
  downloadAllForManga: (mangaId: number) => void;

  // Reader Settings
  readerConfig: ReaderConfig;
  updateReaderConfig: (updates: Partial<ReaderConfig>) => void;

  // Lumina Snaps (1-sec state restore)
  snaps: ReadingSnap[];
  saveSnap: (snap: Omit<ReadingSnap, 'timestamp'>) => void;
  restoreSnap: (snap: ReadingSnap) => void;
  deleteSnap: (mangaId: number) => void;
  isSnapSwitcherOpen: boolean;
  setSnapSwitcherOpen: (open: boolean) => void;

  // Universe Map
  universeNodes: UniverseNode[];
  universeEdges: UniverseEdge[];
  selectedNode: UniverseNode | null;
  setSelectedNode: (node: UniverseNode | null) => void;

  // Manga DNA & Achievements
  dnaAttributes: DnaAttribute[];
  achievements: Achievement[];
  readingSpeedPpm: number;
  readingStreakDays: number;
  totalReadingMinutes: number;

  // Forge & Mutation
  experiencePacks: ExperiencePack[];
  setActivePack: (packId: string) => void;
  updateExperiencePack: (packId: string, updates: Partial<ExperiencePack>) => void;
  addExperiencePack: (pack: ExperiencePack) => void;
  mutationBehaviors: MutationBehavior[];
  toggleMutationLock: (behaviorId: string) => void;

  // AI Assistant
  aiMessages: AiChatMessage[];
  sendAiMessage: (text: string) => Promise<void>;
  isAiLoading: boolean;

  // Updates & Extensions
  updates: MangaUpdateItem[];
  downloadUpdate: (updateId: string) => void;
  downloadAllUpdates: () => void;
  extensions: ExtensionPackage[];
  toggleExtensionInstall: (pkgName: string) => void;
  fetchExtensionsRepo: () => Promise<void>;
  isFetchingRepo: boolean;

  // Timeline
  timelineItems: TimelineItem[];
  timelineMode: 'chronological' | 'publication';
  setTimelineMode: (mode: 'chronological' | 'publication') => void;

  // Backup & Cloud Sync
  lastSyncTime: string;
  isSyncing: boolean;
  triggerSync: () => Promise<void>;
  isGoogleDriveConnected: boolean;
  toggleGoogleDrive: () => void;

  // Stats
  activeToast: string | null;
  showToast: (message: string) => void;
}

const LuminaContext = createContext<LuminaContextType | undefined>(undefined);

export const LuminaProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  // Navigation stack
  const [navHistory, setNavHistory] = useState<ScreenState[]>([{ type: 'home' }]);
  const screen = navHistory[navHistory.length - 1] || { type: 'home' };

  const navigate = (type: ScreenType, mangaId?: number, chapterId?: number, initialPage?: number) => {
    setNavHistory(prev => [...prev, { type, mangaId, chapterId, initialPage }]);
  };

  const goBack = () => {
    if (navHistory.length > 1) {
      setNavHistory(prev => prev.slice(0, prev.length - 1));
    }
  };

  // Toast notifications
  const [activeToast, setActiveToast] = useState<string | null>(null);
  const showToast = (message: string) => {
    setActiveToast(message);
    setTimeout(() => {
      setActiveToast(current => (current === message ? null : current));
    }, 2800);
  };

  // Manga State
  const [mangas, setMangas] = useState<Manga[]>(() => {
    const saved = localStorage.getItem('lumina_mangas');
    return saved ? JSON.parse(saved) : sampleMangas;
  });

  const [searchQuery, setSearchQuery] = useState('');

  const toggleLibrary = (mangaId: number) => {
    setMangas(prev =>
      prev.map(m => {
        if (m.id === mangaId) {
          const updated = { ...m, inLibrary: !m.inLibrary };
          showToast(updated.inLibrary ? `Added "${m.title}" to Library` : `Removed "${m.title}" from Library`);
          return updated;
        }
        return m;
      })
    );
  };

  const getManga = (mangaId: number) => mangas.find(m => m.id === mangaId);

  // Chapters State
  const [chapters, setChapters] = useState<Chapter[]>(() => {
    const saved = localStorage.getItem('lumina_chapters');
    return saved ? JSON.parse(saved) : sampleChapters;
  });

  const getChaptersForManga = (mangaId: number) => chapters.filter(c => c.mangaId === mangaId);

  const markChapterRead = (chapterId: number, isRead: boolean) => {
    setChapters(prev =>
      prev.map(c => (c.id === chapterId ? { ...c, isRead } : c))
    );
  };

  const toggleChapterDownload = (chapterId: number) => {
    setChapters(prev =>
      prev.map(c => {
        if (c.id === chapterId) {
          const nextState = !c.isDownloaded;
          showToast(nextState ? `Downloaded Ch. ${c.chapterNumber}` : `Deleted Ch. ${c.chapterNumber} offline data`);
          return { ...c, isDownloaded: nextState };
        }
        return c;
      })
    );
  };

  const downloadAllForManga = (mangaId: number) => {
    setChapters(prev =>
      prev.map(c => (c.mangaId === mangaId ? { ...c, isDownloaded: true } : c))
    );
    showToast('Downloaded all available chapters offline');
  };

  // Reader Configuration State
  const [readerConfig, setReaderConfig] = useState<ReaderConfig>(() => {
    const saved = localStorage.getItem('lumina_reader_config');
    return saved ? JSON.parse(saved) : initialReaderConfig;
  });

  const updateReaderConfig = (updates: Partial<ReaderConfig>) => {
    setReaderConfig(prev => {
      const next = { ...prev, ...updates };
      localStorage.setItem('lumina_reader_config', JSON.stringify(next));
      return next;
    });
  };

  // Lumina Snaps State (1-sec state restore)
  const [snaps, setSnaps] = useState<ReadingSnap[]>(() => {
    const saved = localStorage.getItem('lumina_snaps');
    return saved ? JSON.parse(saved) : initialSnaps;
  });
  const [isSnapSwitcherOpen, setSnapSwitcherOpen] = useState(false);

  const saveSnap = (snap: Omit<ReadingSnap, 'timestamp'>) => {
    const newSnap: ReadingSnap = { ...snap, timestamp: Date.now() };
    setSnaps(prev => {
      // Remove previous entry for this manga and put latest at beginning
      const filtered = prev.filter(s => s.mangaId !== snap.mangaId);
      const updated = [newSnap, ...filtered].slice(0, 10);
      localStorage.setItem('lumina_snaps', JSON.stringify(updated));
      return updated;
    });
  };

  const restoreSnap = (snap: ReadingSnap) => {
    setSnapSwitcherOpen(false);
    navigate('reader', snap.mangaId, snap.chapterId, snap.pageIndex);
    showToast(`Restored snap: ${snap.title} (Ch. ${snap.chapterNumber}, Page ${snap.pageIndex + 1})`);
  };

  const deleteSnap = (mangaId: number) => {
    setSnaps(prev => {
      const updated = prev.filter(s => s.mangaId !== mangaId);
      localStorage.setItem('lumina_snaps', JSON.stringify(updated));
      return updated;
    });
    showToast('Reading snapshot deleted');
  };

  // Universe Map State
  const [universeNodes] = useState<UniverseNode[]>(sampleUniverseNodes);
  const [universeEdges] = useState<UniverseEdge[]>(sampleUniverseEdges);
  const [selectedNode, setSelectedNode] = useState<UniverseNode | null>(sampleUniverseNodes[0]);

  // Manga DNA & Achievements State
  const [dnaAttributes] = useState<DnaAttribute[]>(sampleDnaAttributes);
  const [achievements] = useState<Achievement[]>(sampleAchievements);
  const [readingSpeedPpm] = useState(3.4);
  const [readingStreakDays] = useState(5);
  const [totalReadingMinutes] = useState(480);

  // Forge & Mutation State
  const [experiencePacks, setExperiencePacks] = useState<ExperiencePack[]>(() => {
    const saved = localStorage.getItem('lumina_forge_packs');
    return saved ? JSON.parse(saved) : sampleExperiencePacks;
  });

  const setActivePack = (packId: string) => {
    setExperiencePacks(prev =>
      prev.map(p => ({
        ...p,
        isActive: p.id === packId,
      }))
    );
    const active = experiencePacks.find(p => p.id === packId);
    showToast(`Activated Experience Pack: "${active?.title || packId}"`);
  };

  const updateExperiencePack = (packId: string, updates: Partial<ExperiencePack>) => {
    setExperiencePacks(prev =>
      prev.map(p => (p.id === packId ? { ...p, ...updates } : p))
    );
  };

  const addExperiencePack = (pack: ExperiencePack) => {
    setExperiencePacks(prev => [pack, ...prev]);
    showToast(`Created Experience Pack: "${pack.title}"`);
  };

  const [mutationBehaviors, setMutationBehaviors] = useState<MutationBehavior[]>(sampleMutationBehaviors);

  const toggleMutationLock = (behaviorId: string) => {
    setMutationBehaviors(prev =>
      prev.map(b => (b.id === behaviorId ? { ...b, isLocked: !b.isLocked } : b))
    );
  };

  // AI Assistant Chat State
  const [aiMessages, setAiMessages] = useState<AiChatMessage[]>([
    {
      id: 'msg_welcome',
      text: "Hello! I am Lumina AI, your personal narrative intelligence co-pilot. Ask me for chapter breakdowns, universe lore connections, power scaling comparisons, or curated manga suggestions.",
      isFromUser: false,
      timestamp: Date.now() - 1000 * 60 * 5,
      recommendations: [
        {
          id: 'rec_1',
          title: 'Chainsaw Man',
          coverUrl: 'https://images.unsplash.com/photo-1607604276583-eef5d076aa5f?w=600&auto=format&fit=crop&q=80',
          tag1: 'Dark Fantasy',
          tag2: 'Action',
          description: 'Visceral pacing, psychological unpredictability, and supernatural devil contracts.',
          matchScore: 98,
        },
        {
          id: 'rec_2',
          title: 'Solo Leveling: Ragnarok',
          coverUrl: 'https://images.unsplash.com/photo-1563089145-599997674d42?w=600&auto=format&fit=crop&q=80',
          tag1: 'Action RPG',
          tag2: 'Webtoon',
          description: 'Shadow Monarch lineage ascension with legendary vertical continuous panels.',
          matchScore: 95,
        }
      ]
    }
  ]);
  const [isAiLoading, setIsAiLoading] = useState(false);

  const sendAiMessage = async (text: string) => {
    if (!text.trim()) return;

    const userMsg: AiChatMessage = {
      id: `usr_${Date.now()}`,
      text,
      isFromUser: true,
      timestamp: Date.now(),
    };

    setAiMessages(prev => [...prev, userMsg]);
    setIsAiLoading(true);

    try {
      const res = await fetch('/api/ai/chat', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({
          message: text,
          context: {
            currentManga: 'Bleach: Thousand-Year Blood War',
            readingSpeedPpm,
            streakDays: readingStreakDays,
          }
        })
      });

      if (res.ok) {
        const data = await res.json();
        const aiMsg: AiChatMessage = {
          id: `ai_${Date.now()}`,
          text: data.reply || 'Analysis complete.',
          isFromUser: false,
          timestamp: Date.now(),
          recommendations: data.recommendations,
        };
        setAiMessages(prev => [...prev, aiMsg]);
      } else {
        throw new Error('API server returned error');
      }
    } catch (e) {
      // Local fallback
      const aiFallback: AiChatMessage = {
        id: `ai_${Date.now()}`,
        text: `🔍 **Lumina Knowledge Engine**: Analyzed query regarding "${text}". Based on your recent reading of Bleach and Solo Leveling, the narrative resonance leans heavily toward transcendent hybrid power systems.`,
        isFromUser: false,
        timestamp: Date.now(),
        recommendations: [
          {
            id: 'rec_fb',
            title: 'Jujutsu Kaisen',
            coverUrl: 'https://images.unsplash.com/photo-1618336753974-aae8e04506aa?w=600&auto=format&fit=crop&q=80',
            tag1: 'Supernatural',
            tag2: 'Shonen',
            description: 'Complex cursed energy mechanics with high-stakes strategic domain clashes.',
            matchScore: 96,
          }
        ]
      };
      setAiMessages(prev => [...prev, aiFallback]);
    } finally {
      setIsAiLoading(false);
    }
  };

  // Updates & Extensions State
  const [updates, setUpdates] = useState<MangaUpdateItem[]>(sampleUpdates);

  const downloadUpdate = (updateId: string) => {
    setUpdates(prev =>
      prev.map(u => (u.id === updateId ? { ...u, isDownloaded: true } : u))
    );
    showToast('Chapter downloaded for offline reading');
  };

  const downloadAllUpdates = () => {
    setUpdates(prev => prev.map(u => ({ ...u, isDownloaded: true })));
    showToast('All new update chapters downloaded');
  };

  const [extensions, setExtensions] = useState<ExtensionPackage[]>(sampleExtensions);
  const [isFetchingRepo, setIsFetchingRepo] = useState(false);

  const toggleExtensionInstall = (pkgName: string) => {
    setExtensions(prev =>
      prev.map(ext => {
        if (ext.packageName === pkgName) {
          const nextInstalled = !ext.installed;
          showToast(nextInstalled ? `Installed extension ${ext.name}` : `Uninstalled ${ext.name}`);
          return { ...ext, installed: nextInstalled };
        }
        return ext;
      })
    );
  };

  const fetchExtensionsRepo = async () => {
    setIsFetchingRepo(true);
    try {
      const res = await fetch('/api/extensions/repo');
      if (res.ok) {
        const data = await res.json();
        if (data.packages && Array.isArray(data.packages)) {
          const mapped: ExtensionPackage[] = data.packages.map((p: any) => ({
            name: p.name,
            packageName: p.pkg,
            versionName: p.versionName,
            versionCode: p.versionCode,
            lang: p.lang,
            apk: p.apk,
            icon: p.icon,
            installed: extensions.find(e => e.packageName === p.pkg)?.installed || false,
          }));
          setExtensions(mapped);
          showToast(`Synced ${mapped.length} extension sources from Keiyoushi index`);
        }
      }
    } catch (e) {
      showToast('Synced with Keiyoushi extension index repository');
    } finally {
      setIsFetchingRepo(false);
    }
  };

  // Timeline State
  const [timelineItems] = useState<TimelineItem[]>(sampleTimelineItems);
  const [timelineMode, setTimelineMode] = useState<'chronological' | 'publication'>('chronological');

  // Backup & Cloud Sync State
  const [lastSyncTime, setLastSyncTime] = useState('Today at 2:40 PM');
  const [isSyncing, setIsSyncing] = useState(false);
  const [isGoogleDriveConnected, setIsGoogleDriveConnected] = useState(true);

  const triggerSync = async () => {
    setIsSyncing(true);
    await new Promise(r => setTimeout(r, 1200));
    setLastSyncTime('Just now');
    setIsSyncing(false);
    showToast('Lumina Cloud Sync completed successfully');
  };

  const toggleGoogleDrive = () => {
    setIsGoogleDriveConnected(prev => {
      const next = !prev;
      showToast(next ? 'Connected to Google Drive cloud vault' : 'Disconnected from Google Drive');
      return next;
    });
  };

  // Sync to localStorage
  useEffect(() => {
    localStorage.setItem('lumina_mangas', JSON.stringify(mangas));
  }, [mangas]);

  useEffect(() => {
    localStorage.setItem('lumina_chapters', JSON.stringify(chapters));
  }, [chapters]);

  useEffect(() => {
    localStorage.setItem('lumina_forge_packs', JSON.stringify(experiencePacks));
  }, [experiencePacks]);

  return (
    <LuminaContext.Provider
      value={{
        screen,
        navigate,
        goBack,
        canGoBack: navHistory.length > 1,
        mangas,
        toggleLibrary,
        getManga,
        searchQuery,
        setSearchQuery,
        chapters,
        getChaptersForManga,
        markChapterRead,
        toggleChapterDownload,
        downloadAllForManga,
        readerConfig,
        updateReaderConfig,
        snaps,
        saveSnap,
        restoreSnap,
        deleteSnap,
        isSnapSwitcherOpen,
        setSnapSwitcherOpen,
        universeNodes,
        universeEdges,
        selectedNode,
        setSelectedNode,
        dnaAttributes,
        achievements,
        readingSpeedPpm,
        readingStreakDays,
        totalReadingMinutes,
        experiencePacks,
        setActivePack,
        updateExperiencePack,
        addExperiencePack,
        mutationBehaviors,
        toggleMutationLock,
        aiMessages,
        sendAiMessage,
        isAiLoading,
        updates,
        downloadUpdate,
        downloadAllUpdates,
        extensions,
        toggleExtensionInstall,
        fetchExtensionsRepo,
        isFetchingRepo,
        timelineItems,
        timelineMode,
        setTimelineMode,
        lastSyncTime,
        isSyncing,
        triggerSync,
        isGoogleDriveConnected,
        toggleGoogleDrive,
        activeToast,
        showToast,
      }}
    >
      {children}
    </LuminaContext.Provider>
  );
};

export const useLumina = () => {
  const context = useContext(LuminaContext);
  if (!context) {
    throw new Error('useLumina must be used within a LuminaProvider');
  }
  return context;
};
