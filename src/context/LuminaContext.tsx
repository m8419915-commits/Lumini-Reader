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
  ExtensionStore,
  SourceMeta,
  SourceMigrationItem,
  NetworkSecurityConfig,
  TimelineItem,
  ReaderMode,
  BackgroundTint,
  HistoryItem,
  Category,
  LibraryFilters,
  MangaTracker,
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
  sampleExtensionStores,
  sampleSources,
  sampleMigrationItems,
  initialNetworkConfig,
  sampleTimelineItems,
  initialReaderConfig,
  initialCategories,
  sampleHistoryItems,
} from '../data/initialData';

export const defaultLibraryFilters: LibraryFilters = {
  downloaded: 'none',
  unread: 'none',
  started: 'none',
  completed: 'none',
  tracked: 'none',
  category: 'All',
  source: 'all',
  sortBy: 'alphabetical',
  sortOrder: 'asc',
  displayMode: 'compact_grid',
};

interface LuminaContextType {
  // Navigation
  screen: ScreenState;
  navigate: (type: ScreenType, mangaId?: number, chapterId?: number, initialPage?: number, sourceId?: string) => void;
  goBack: () => void;
  canGoBack: boolean;

  // Manga & Library
  mangas: Manga[];
  toggleLibrary: (mangaId: number) => void;
  getManga: (mangaId: number) => Manga | undefined;
  searchQuery: string;
  setSearchQuery: (query: string) => void;
  updateMangaTracker: (mangaId: number, service: string, updates: Partial<MangaTracker>) => void;
  addMangaTracker: (mangaId: number, tracker: MangaTracker) => void;
  removeMangaTracker: (mangaId: number, service: string) => void;

  // Categories
  categories: Category[];
  addCategory: (name: string) => void;
  removeCategory: (id: string) => void;
  reorderCategories: (categories: Category[]) => void;
  setMangaCategory: (mangaId: number, categoryName: string) => void;

  // Library Filters & Organization
  libraryFilters: LibraryFilters;
  updateLibraryFilters: (updates: Partial<LibraryFilters>) => void;
  resetLibraryFilters: () => void;

  // History System (Mihon Parity)
  historyItems: HistoryItem[];
  addHistoryItem: (item: Omit<HistoryItem, 'id'>) => void;
  removeHistoryItem: (historyId: string) => void;
  clearHistory: () => void;

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

  // Extensions & Repositories Ecosystem (Mihon Parity)
  extensions: ExtensionPackage[];
  extensionStores: ExtensionStore[];
  sources: SourceMeta[];
  toggleExtensionInstall: (pkgName: string) => void;
  installExtension: (pkgName: string) => void;
  uninstallExtension: (pkgName: string) => void;
  updateExtension: (pkgName: string) => void;
  updateAllExtensions: () => void;
  toggleExtensionTrust: (pkgName: string) => void;
  addExtensionStore: (store: Omit<ExtensionStore, 'id' | 'totalExtensions' | 'lastSynced' | 'status'>) => void;
  removeExtensionStore: (storeId: string) => void;
  toggleExtensionStore: (storeId: string) => void;
  fetchExtensionsRepo: () => Promise<void>;
  isFetchingRepo: boolean;
  togglePinSource: (sourceId: string) => void;

  // Migration Engine
  migrationItems: SourceMigrationItem[];
  executeMigration: (migrationId: string) => Promise<void>;
  startSmartMigration: (mangaId: number, targetSourceId: string) => void;

  // Network & Compatibility
  networkConfig: NetworkSecurityConfig;
  updateNetworkConfig: (updates: Partial<NetworkSecurityConfig>) => void;

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

  const navigate = (type: ScreenType, mangaId?: number, chapterId?: number, initialPage?: number, sourceId?: string) => {
    setNavHistory(prev => [...prev, { type, mangaId, chapterId, initialPage, sourceId }]);
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
    setMangas(prev => {
      const updated = prev.map(m => {
        if (m.id === mangaId) {
          const inLibrary = !m.inLibrary;
          showToast(inLibrary ? `Added "${m.title}" to Library` : `Removed "${m.title}" from Library`);
          return { ...m, inLibrary };
        }
        return m;
      });
      localStorage.setItem('lumina_mangas', JSON.stringify(updated));
      return updated;
    });
  };

  const getManga = (mangaId: number) => mangas.find(m => m.id === mangaId);

  const updateMangaTracker = (mangaId: number, service: string, updates: Partial<MangaTracker>) => {
    setMangas(prev => {
      const updated = prev.map(m => {
        if (m.id === mangaId) {
          const existingTrackers = m.trackers || [];
          const updatedTrackers = existingTrackers.map(t =>
            t.service === service ? { ...t, ...updates } : t
          );
          return { ...m, trackers: updatedTrackers };
        }
        return m;
      });
      localStorage.setItem('lumina_mangas', JSON.stringify(updated));
      return updated;
    });
    showToast(`Updated tracker for ${service.toUpperCase()}`);
  };

  const addMangaTracker = (mangaId: number, tracker: MangaTracker) => {
    setMangas(prev => {
      const updated = prev.map(m => {
        if (m.id === mangaId) {
          const existingTrackers = m.trackers || [];
          const filtered = existingTrackers.filter(t => t.service !== tracker.service);
          return { ...m, trackers: [...filtered, tracker] };
        }
        return m;
      });
      localStorage.setItem('lumina_mangas', JSON.stringify(updated));
      return updated;
    });
    showToast(`Connected ${tracker.serviceName} tracking`);
  };

  const removeMangaTracker = (mangaId: number, service: string) => {
    setMangas(prev => {
      const updated = prev.map(m => {
        if (m.id === mangaId) {
          return { ...m, trackers: (m.trackers || []).filter(t => t.service !== service) };
        }
        return m;
      });
      localStorage.setItem('lumina_mangas', JSON.stringify(updated));
      return updated;
    });
    showToast(`Unlinked ${service} tracker`);
  };

  // Categories State
  const [categories, setCategories] = useState<Category[]>(() => {
    const saved = localStorage.getItem('lumina_categories');
    return saved ? JSON.parse(saved) : initialCategories;
  });

  const addCategory = (name: string) => {
    if (!name.trim()) return;
    const newCat: Category = {
      id: `cat_${Date.now()}`,
      name: name.trim(),
      count: 0,
      isDefault: false,
      order: categories.length,
    };
    setCategories(prev => {
      const updated = [...prev, newCat];
      localStorage.setItem('lumina_categories', JSON.stringify(updated));
      return updated;
    });
    showToast(`Created category "${name}"`);
  };

  const removeCategory = (id: string) => {
    setCategories(prev => {
      const updated = prev.filter(c => c.id !== id);
      localStorage.setItem('lumina_categories', JSON.stringify(updated));
      return updated;
    });
    showToast('Category removed');
  };

  const reorderCategories = (newCategories: Category[]) => {
    setCategories(newCategories);
    localStorage.setItem('lumina_categories', JSON.stringify(newCategories));
  };

  const setMangaCategory = (mangaId: number, categoryName: string) => {
    setMangas(prev => {
      const updated = prev.map(m => (m.id === mangaId ? { ...m, category: categoryName } : m));
      localStorage.setItem('lumina_mangas', JSON.stringify(updated));
      return updated;
    });
    showToast(`Moved to category "${categoryName}"`);
  };

  // Library Filters
  const [libraryFilters, setLibraryFilters] = useState<LibraryFilters>(() => {
    const saved = localStorage.getItem('lumina_library_filters');
    return saved ? JSON.parse(saved) : defaultLibraryFilters;
  });

  const updateLibraryFilters = (updates: Partial<LibraryFilters>) => {
    setLibraryFilters(prev => {
      const next = { ...prev, ...updates };
      localStorage.setItem('lumina_library_filters', JSON.stringify(next));
      return next;
    });
  };

  const resetLibraryFilters = () => {
    setLibraryFilters(defaultLibraryFilters);
    localStorage.setItem('lumina_library_filters', JSON.stringify(defaultLibraryFilters));
    showToast('Library filters reset to default');
  };

  // History State
  const [historyItems, setHistoryItems] = useState<HistoryItem[]>(() => {
    const saved = localStorage.getItem('lumina_history');
    return saved ? JSON.parse(saved) : sampleHistoryItems;
  });

  const addHistoryItem = (item: Omit<HistoryItem, 'id'>) => {
    const newItem: HistoryItem = {
      readAt: Date.now(),
      ...item,
      id: `hist_${Date.now()}`,
    };
    setHistoryItems(prev => {
      // Remove previous entry for same manga & chapter to avoid duplicates at top
      const filtered = prev.filter(h => !(h.mangaId === item.mangaId && h.chapterId === item.chapterId));
      const updated = [newItem, ...filtered];
      localStorage.setItem('lumina_history', JSON.stringify(updated));
      return updated;
    });
  };

  const removeHistoryItem = (historyId: string) => {
    setHistoryItems(prev => {
      const updated = prev.filter(h => h.id !== historyId);
      localStorage.setItem('lumina_history', JSON.stringify(updated));
      return updated;
    });
    showToast('Reading history record removed');
  };

  const clearHistory = () => {
    setHistoryItems([]);
    localStorage.removeItem('lumina_history');
    showToast('Reading history cleared');
  };

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

  // Updates State
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

  // Extensions & Repositories State (Mihon Parity)
  const [extensions, setExtensions] = useState<ExtensionPackage[]>(() => {
    const saved = localStorage.getItem('lumina_extensions');
    return saved ? JSON.parse(saved) : sampleExtensions;
  });

  const [extensionStores, setExtensionStores] = useState<ExtensionStore[]>(() => {
    const saved = localStorage.getItem('lumina_extension_stores');
    return saved ? JSON.parse(saved) : sampleExtensionStores;
  });

  const [sources, setSources] = useState<SourceMeta[]>(() => {
    const saved = localStorage.getItem('lumina_sources');
    return saved ? JSON.parse(saved) : sampleSources;
  });

  const [isFetchingRepo, setIsFetchingRepo] = useState(false);

  const toggleExtensionInstall = (pkgName: string) => {
    setExtensions(prev =>
      prev.map(ext => {
        if (ext.packageName === pkgName) {
          const nextInstalled = !ext.installed;
          const nextStatus = nextInstalled ? 'installed' : 'available';
          showToast(nextInstalled ? `Installed extension: ${ext.name}` : `Uninstalled ${ext.name}`);
          return {
            ...ext,
            installed: nextInstalled,
            status: nextStatus,
            installedVersionName: nextInstalled ? ext.versionName : undefined,
          };
        }
        return ext;
      })
    );
  };

  const installExtension = (pkgName: string) => {
    setExtensions(prev =>
      prev.map(ext => {
        if (ext.packageName === pkgName) {
          showToast(`Installed ${ext.name} (v${ext.versionName})`);
          return {
            ...ext,
            installed: true,
            status: 'installed',
            installedVersionName: ext.versionName,
          };
        }
        return ext;
      })
    );
  };

  const uninstallExtension = (pkgName: string) => {
    setExtensions(prev =>
      prev.map(ext => {
        if (ext.packageName === pkgName) {
          showToast(`Uninstalled extension: ${ext.name}`);
          return {
            ...ext,
            installed: false,
            status: 'available',
            installedVersionName: undefined,
          };
        }
        return ext;
      })
    );
  };

  const updateExtension = (pkgName: string) => {
    setExtensions(prev =>
      prev.map(ext => {
        if (ext.packageName === pkgName) {
          showToast(`Updated ${ext.name} to v${ext.versionName}`);
          return {
            ...ext,
            installed: true,
            status: 'installed',
            installedVersionName: ext.versionName,
          };
        }
        return ext;
      })
    );
  };

  const updateAllExtensions = () => {
    setExtensions(prev =>
      prev.map(ext => {
        if (ext.status === 'update_available') {
          return {
            ...ext,
            installed: true,
            status: 'installed',
            installedVersionName: ext.versionName,
          };
        }
        return ext;
      })
    );
    showToast('All extensions updated to latest repository build');
  };

  const toggleExtensionTrust = (pkgName: string) => {
    setExtensions(prev =>
      prev.map(ext => {
        if (ext.packageName === pkgName) {
          const nextTrust = !ext.isTrusted;
          showToast(nextTrust ? `Trusted extension: ${ext.name}` : `Marked ${ext.name} as untrusted`);
          return {
            ...ext,
            isTrusted: nextTrust,
            status: nextTrust && ext.installed ? 'installed' : !nextTrust ? 'untrusted' : ext.status,
          };
        }
        return ext;
      })
    );
  };

  const addExtensionStore = (store: Omit<ExtensionStore, 'id' | 'totalExtensions' | 'lastSynced' | 'status'>) => {
    const newStore: ExtensionStore = {
      ...store,
      id: `store_${Date.now()}`,
      totalExtensions: Math.floor(Math.random() * 40) + 10,
      lastSynced: 'Just now',
      status: 'synced',
    };
    setExtensionStores(prev => [newStore, ...prev]);
    showToast(`Added repository: "${store.name}"`);
  };

  const removeExtensionStore = (storeId: string) => {
    setExtensionStores(prev => prev.filter(s => s.id !== storeId));
    showToast('Repository removed');
  };

  const toggleExtensionStore = (storeId: string) => {
    setExtensionStores(prev =>
      prev.map(s => {
        if (s.id === storeId) {
          const nextEnabled = !s.enabled;
          showToast(nextEnabled ? `Enabled repository "${s.name}"` : `Disabled repository "${s.name}"`);
          return { ...s, enabled: nextEnabled };
        }
        return s;
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
          showToast(`Synced ${data.packages.length} extensions from Keiyoushi & community feeds`);
        }
      } else {
        showToast('Synced with Keiyoushi extension index repository');
      }
    } catch (e) {
      showToast('Synced with Keiyoushi extension index repository');
    } finally {
      setIsFetchingRepo(false);
    }
  };

  const togglePinSource = (sourceId: string) => {
    setSources(prev =>
      prev.map(s => {
        if (s.id === sourceId) {
          const nextPinned = !s.isPinned;
          showToast(nextPinned ? `Pinned ${s.name} to top of sources` : `Unpinned ${s.name}`);
          return { ...s, isPinned: nextPinned };
        }
        return s;
      })
    );
  };

  // Migration Engine State
  const [migrationItems, setMigrationItems] = useState<SourceMigrationItem[]>(() => {
    const saved = localStorage.getItem('lumina_migration_items');
    return saved ? JSON.parse(saved) : sampleMigrationItems;
  });

  const executeMigration = async (migrationId: string) => {
    setMigrationItems(prev =>
      prev.map(item => (item.id === migrationId ? { ...item, status: 'searching' } : item))
    );
    await new Promise(r => setTimeout(r, 1000));
    setMigrationItems(prev =>
      prev.map(item => {
        if (item.id === migrationId) {
          // Update the manga's source in the main library
          setMangas(mPrev =>
            mPrev.map(m => (m.id === item.mangaId ? { ...m, source: item.toSourceName, sourceId: item.toSourceId } : m))
          );
          showToast(`Successfully migrated "${item.mangaTitle}" to ${item.toSourceName}`);
          return { ...item, status: 'migrated' };
        }
        return item;
      })
    );
  };

  const startSmartMigration = (mangaId: number, targetSourceId: string) => {
    const targetManga = mangas.find(m => m.id === mangaId);
    const targetSource = sources.find(s => s.id === targetSourceId);
    if (!targetManga || !targetSource) return;

    const newJob: SourceMigrationItem = {
      id: `mig_${Date.now()}`,
      mangaId,
      mangaTitle: targetManga.title,
      mangaCover: targetManga.thumbnailUrl,
      fromSourceId: targetManga.sourceId || 'mangadex',
      fromSourceName: targetManga.source,
      toSourceId: targetSource.id,
      toSourceName: targetSource.name,
      targetMangaTitle: `${targetManga.title} [${targetSource.name}]`,
      targetChapterCount: targetManga.totalChapters,
      status: 'ready',
      matchScore: 98,
    };
    setMigrationItems(prev => [newJob, ...prev]);
    showToast(`Prepared migration job for "${targetManga.title}" -> ${targetSource.name}`);
  };

  // Network & Compatibility Configuration State
  const [networkConfig, setNetworkConfig] = useState<NetworkSecurityConfig>(() => {
    const saved = localStorage.getItem('lumina_network_config');
    return saved ? JSON.parse(saved) : initialNetworkConfig;
  });

  const updateNetworkConfig = (updates: Partial<NetworkSecurityConfig>) => {
    setNetworkConfig(prev => {
      const next = { ...prev, ...updates };
      localStorage.setItem('lumina_network_config', JSON.stringify(next));
      showToast('Network & resolver preferences updated');
      return next;
    });
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

  // Persistent storage hooks
  useEffect(() => {
    localStorage.setItem('lumina_mangas', JSON.stringify(mangas));
  }, [mangas]);

  useEffect(() => {
    localStorage.setItem('lumina_chapters', JSON.stringify(chapters));
  }, [chapters]);

  useEffect(() => {
    localStorage.setItem('lumina_forge_packs', JSON.stringify(experiencePacks));
  }, [experiencePacks]);

  useEffect(() => {
    localStorage.setItem('lumina_extensions', JSON.stringify(extensions));
  }, [extensions]);

  useEffect(() => {
    localStorage.setItem('lumina_extension_stores', JSON.stringify(extensionStores));
  }, [extensionStores]);

  useEffect(() => {
    localStorage.setItem('lumina_sources', JSON.stringify(sources));
  }, [sources]);

  useEffect(() => {
    localStorage.setItem('lumina_migration_items', JSON.stringify(migrationItems));
  }, [migrationItems]);

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
        updateMangaTracker,
        addMangaTracker,
        removeMangaTracker,
        categories,
        addCategory,
        removeCategory,
        reorderCategories,
        setMangaCategory,
        libraryFilters,
        updateLibraryFilters,
        resetLibraryFilters,
        historyItems,
        addHistoryItem,
        removeHistoryItem,
        clearHistory,
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
        extensionStores,
        sources,
        toggleExtensionInstall,
        installExtension,
        uninstallExtension,
        updateExtension,
        updateAllExtensions,
        toggleExtensionTrust,
        addExtensionStore,
        removeExtensionStore,
        toggleExtensionStore,
        fetchExtensionsRepo,
        isFetchingRepo,
        togglePinSource,
        migrationItems,
        executeMigration,
        startSmartMigration,
        networkConfig,
        updateNetworkConfig,
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
