import React, { useState, useMemo } from 'react';
import {
  Search,
  Filter,
  BookOpen,
  Compass,
  Plus,
  ArrowUpDown,
  LayoutGrid,
  Grid,
  List,
  SlidersHorizontal,
  Check,
  X,
  Minus,
  Sparkles,
  Play,
  Download,
  Eye,
  CheckCircle2,
  FolderPlus,
  Trash2,
  MoreVertical,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { HeaderBar } from '../navigation/HeaderBar';
import { TriState, LibraryFilters, Category, Manga } from '../../types';

export const LibraryScreen: React.FC = () => {
  const {
    mangas,
    navigate,
    categories,
    addCategory,
    removeCategory,
    setMangaCategory,
    libraryFilters,
    updateLibraryFilters,
    resetLibraryFilters,
    getChaptersForManga,
  } = useLumina();

  const [search, setSearch] = useState('');
  const [selectedCategory, setSelectedCategory] = useState<string>('All');
  const [isFilterSheetOpen, setIsFilterSheetOpen] = useState(false);
  const [isCategoryModalOpen, setIsCategoryModalOpen] = useState(false);
  const [newCatName, setNewCatName] = useState('');
  const [managingMangaId, setManagingMangaId] = useState<number | null>(null);

  // Compute category counts
  const categoryCounts = useMemo(() => {
    const counts: { [key: string]: number } = { All: 0 };
    mangas.forEach(m => {
      if (m.inLibrary) {
        counts.All = (counts.All || 0) + 1;
        const cat = m.category || 'Reading';
        counts[cat] = (counts[cat] || 0) + 1;
      }
    });
    return counts;
  }, [mangas]);

  // Next Tri-State helper
  const nextTriState = (curr: TriState): TriState => {
    if (curr === 'none') return 'included';
    if (curr === 'included') return 'excluded';
    return 'none';
  };

  // Filter & Sort Pipeline
  const processedMangas = useMemo(() => {
    return mangas.filter(m => {
      // 1. Library membership
      if (!m.inLibrary && selectedCategory !== 'All') return false;

      // 2. Search Query
      if (search.trim()) {
        const q = search.toLowerCase();
        const matches =
          m.title.toLowerCase().includes(q) ||
          m.author.toLowerCase().includes(q) ||
          m.genre.some(g => g.toLowerCase().includes(q)) ||
          m.source.toLowerCase().includes(q);
        if (!matches) return false;
      }

      // 3. Category Filter
      if (selectedCategory !== 'All') {
        const cat = m.category || 'Reading';
        if (cat.toLowerCase() !== selectedCategory.toLowerCase()) return false;
      }

      // 4. Downloaded Tri-state filter
      if (libraryFilters.downloaded !== 'none') {
        const chapters = getChaptersForManga(m.id);
        const hasDownloaded = chapters.some(c => c.isDownloaded);
        if (libraryFilters.downloaded === 'included' && !hasDownloaded) return false;
        if (libraryFilters.downloaded === 'excluded' && hasDownloaded) return false;
      }

      // 5. Unread Tri-state filter
      if (libraryFilters.unread !== 'none') {
        const isUnread = (m.unreadCount || 0) > 0;
        if (libraryFilters.unread === 'included' && !isUnread) return false;
        if (libraryFilters.unread === 'excluded' && isUnread) return false;
      }

      // 6. Started Tri-state filter
      if (libraryFilters.started !== 'none') {
        const isStarted = !!m.lastReadAt;
        if (libraryFilters.started === 'included' && !isStarted) return false;
        if (libraryFilters.started === 'excluded' && isStarted) return false;
      }

      // 7. Completed Tri-state filter
      if (libraryFilters.completed !== 'none') {
        const isCompleted = m.status === 'Completed';
        if (libraryFilters.completed === 'included' && !isCompleted) return false;
        if (libraryFilters.completed === 'excluded' && isCompleted) return false;
      }

      // 8. Tracked Tri-state filter
      if (libraryFilters.tracked !== 'none') {
        const isTracked = (m.trackers && m.trackers.length > 0) || false;
        if (libraryFilters.tracked === 'included' && !isTracked) return false;
        if (libraryFilters.tracked === 'excluded' && isTracked) return false;
      }

      // 9. Source filter
      if (libraryFilters.source !== 'all' && m.sourceId !== libraryFilters.source) {
        return false;
      }

      return true;
    }).sort((a, b) => {
      const order = libraryFilters.sortOrder === 'asc' ? 1 : -1;
      switch (libraryFilters.sortBy) {
        case 'alphabetical':
          return order * a.title.localeCompare(b.title);
        case 'lastRead':
          return order * ((b.lastReadAt || 0) - (a.lastReadAt || 0));
        case 'totalChapters':
          return order * (a.totalChapters - b.totalChapters);
        case 'unreadCount':
          return order * ((a.unreadCount || 0) - (b.unreadCount || 0));
        case 'latestUpdate':
          return order * (b.id - a.id);
        case 'dateAdded':
        default:
          return order * (a.id - b.id);
      }
    });
  }, [mangas, selectedCategory, search, libraryFilters, getChaptersForManga]);

  const activeFiltersCount = useMemo(() => {
    let count = 0;
    if (libraryFilters.downloaded !== 'none') count++;
    if (libraryFilters.unread !== 'none') count++;
    if (libraryFilters.started !== 'none') count++;
    if (libraryFilters.completed !== 'none') count++;
    if (libraryFilters.tracked !== 'none') count++;
    if (libraryFilters.source !== 'all') count++;
    return count;
  }, [libraryFilters]);

  const handleQuickResume = (e: React.MouseEvent, manga: Manga) => {
    e.stopPropagation();
    const chapters = getChaptersForManga(manga.id);
    const unreadChap = chapters.find(c => !c.isRead) || chapters[0];
    if (unreadChap) {
      navigate('reader', manga.id, unreadChap.id, 0);
    } else {
      navigate('detail', manga.id);
    }
  };

  return (
    <div className="min-h-screen bg-[#0C0C10] text-white pb-24 selection:bg-[#8B5CF6]/30">
      <HeaderBar />

      <main className="max-w-3xl mx-auto px-4 pt-3 space-y-3.5">
        {/* Search Bar & Filter Action Header */}
        <div className="flex items-center gap-2">
          <div className="relative flex-1">
            <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-zinc-400" />
            <input
              type="text"
              value={search}
              onChange={e => setSearch(e.target.value)}
              placeholder="Search library, authors, genres..."
              className="w-full bg-[#15151D] border border-[#262633] focus:border-[#8B5CF6] rounded-xl pl-10 pr-4 py-2.5 text-sm text-zinc-100 placeholder-zinc-500 outline-none transition-all shadow-inner"
            />
            {search && (
              <button
                onClick={() => setSearch('')}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-xs text-zinc-500 hover:text-zinc-300 p-1"
              >
                <X className="w-3.5 h-3.5" />
              </button>
            )}
          </div>

          <button
            onClick={() => setIsFilterSheetOpen(true)}
            className={`relative p-2.5 rounded-xl border flex items-center justify-center transition-all ${
              activeFiltersCount > 0
                ? 'bg-[#8B5CF6]/20 border-[#8B5CF6] text-[#A78BFA] shadow-[0_0_12px_rgba(139,92,246,0.3)]'
                : 'bg-[#15151D] border-[#262633] text-zinc-400 hover:text-white hover:border-[#38384C]'
            }`}
            title="Filter & Sort Display"
          >
            <SlidersHorizontal className="w-4 h-4" />
            {activeFiltersCount > 0 && (
              <span className="absolute -top-1 -right-1 w-4 h-4 rounded-full bg-[#8B5CF6] text-white text-[10px] font-bold flex items-center justify-center">
                {activeFiltersCount}
              </span>
            )}
          </button>
        </div>

        {/* Category Navigation Pills with Mihon Management */}
        <div className="flex items-center gap-1.5 overflow-x-auto pb-1 no-scrollbar">
          {['All', 'Reading', 'Completed', 'Plan to Read', 'On Hold'].map(cat => {
            const isSelected = selectedCategory === cat;
            const count = categoryCounts[cat] || 0;

            return (
              <button
                key={cat}
                onClick={() => setSelectedCategory(cat)}
                className={`px-3.5 py-1.5 rounded-xl text-xs font-semibold whitespace-nowrap flex items-center gap-1.5 transition-all ${
                  isSelected
                    ? 'bg-[#8B5CF6] text-white shadow-[0_0_12px_rgba(139,92,246,0.4)]'
                    : 'bg-[#15151D] border border-[#242432] text-zinc-400 hover:text-zinc-200'
                }`}
              >
                <span>{cat}</span>
                <span
                  className={`text-[10px] px-1.5 py-0.2 rounded-full font-mono ${
                    isSelected ? 'bg-black/30 text-white' : 'bg-[#20202C] text-zinc-400'
                  }`}
                >
                  {count}
                </span>
              </button>
            );
          })}

          <button
            onClick={() => setIsCategoryModalOpen(true)}
            className="p-1.5 rounded-xl bg-[#15151D] border border-[#242432] text-zinc-400 hover:text-[#A78BFA] transition-colors"
            title="Edit categories"
          >
            <FolderPlus className="w-4 h-4" />
          </button>
        </div>

        {/* Manga Content View based on Display Mode */}
        {processedMangas.length === 0 ? (
          <div className="py-20 text-center space-y-4 px-4">
            <div className="w-14 h-14 mx-auto rounded-2xl bg-[#171722] border border-[#2A2A3C] flex items-center justify-center text-zinc-500">
              <BookOpen className="w-7 h-7 stroke-1" />
            </div>
            <div className="space-y-1">
              <h3 className="text-base font-semibold text-zinc-200">No manga in this view</h3>
              <p className="text-xs text-zinc-500 max-w-xs mx-auto">
                {activeFiltersCount > 0 || search
                  ? 'Try relaxing active search filters or selecting another category.'
                  : 'Add series from the Explore catalog to build your personal library.'}
              </p>
            </div>
            <div className="flex items-center justify-center gap-2 pt-2">
              {activeFiltersCount > 0 && (
                <button
                  onClick={resetLibraryFilters}
                  className="px-4 py-2 rounded-xl bg-[#22222F] text-zinc-300 text-xs font-semibold hover:bg-[#2C2C3C] transition-colors"
                >
                  Reset Filters
                </button>
              )}
              <button
                onClick={() => navigate('explore')}
                className="inline-flex items-center gap-2 px-5 py-2.5 rounded-xl bg-gradient-to-r from-[#8B5CF6] to-[#6366F1] text-white text-xs font-semibold shadow-[0_0_15px_rgba(139,92,246,0.3)] hover:opacity-95 transition-opacity"
              >
                <Compass className="w-4 h-4" />
                Browse Catalog
              </button>
            </div>
          </div>
        ) : libraryFilters.displayMode === 'list' ? (
          /* List Mode */
          <div className="space-y-2">
            {processedMangas.map(manga => {
              const unread = manga.unreadCount || 0;
              const hasTrackers = (manga.trackers && manga.trackers.length > 0) || false;

              return (
                <div
                  key={manga.id}
                  onClick={() => navigate('detail', manga.id)}
                  className="group flex items-center gap-3.5 p-2.5 rounded-2xl bg-[#15151D] hover:bg-[#1A1A26] border border-[#242432] hover:border-[#38384E] transition-all cursor-pointer"
                >
                  <div className="w-12 h-16 rounded-xl overflow-hidden bg-zinc-900 flex-shrink-0 border border-zinc-800 relative">
                    <img
                      src={manga.thumbnailUrl}
                      alt={manga.title}
                      className="w-full h-full object-cover"
                      referrerPolicy="no-referrer"
                      loading="lazy"
                    />
                  </div>

                  <div className="flex-1 min-w-0">
                    <h4 className="text-sm font-semibold text-zinc-100 truncate group-hover:text-[#A78BFA] transition-colors">
                      {manga.title}
                    </h4>
                    <div className="flex items-center gap-2 mt-1 text-[11px] text-zinc-400">
                      <span>{manga.author}</span>
                      <span>•</span>
                      <span className="text-[#00E5FF] font-medium">{manga.source}</span>
                    </div>
                    <div className="flex items-center gap-2 mt-1.5">
                      {unread > 0 ? (
                        <span className="text-[10px] px-2 py-0.5 rounded-md bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 text-[#A78BFA] font-bold font-mono">
                          {unread} unread
                        </span>
                      ) : (
                        <span className="text-[10px] px-2 py-0.5 rounded-md bg-emerald-500/20 text-emerald-400 font-medium">
                          Completed
                        </span>
                      )}
                      {hasTrackers && (
                        <span className="text-[10px] px-1.5 py-0.5 rounded-md bg-[#02A9FF]/20 text-[#02A9FF] font-medium">
                          Tracked
                        </span>
                      )}
                    </div>
                  </div>

                  <div className="flex items-center gap-1.5 flex-shrink-0">
                    <button
                      onClick={e => handleQuickResume(e, manga)}
                      className="w-8 h-8 rounded-xl bg-[#8B5CF6]/20 hover:bg-[#8B5CF6] text-[#A78BFA] hover:text-white flex items-center justify-center transition-colors"
                      title="Read next chapter"
                    >
                      <Play className="w-3.5 h-3.5 fill-current ml-0.5" />
                    </button>
                    <button
                      onClick={e => {
                        e.stopPropagation();
                        setManagingMangaId(manga.id);
                      }}
                      className="w-8 h-8 rounded-xl text-zinc-500 hover:text-zinc-200 flex items-center justify-center transition-colors"
                    >
                      <MoreVertical className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              );
            })}
          </div>
        ) : (
          /* Grid View (Compact / Comfortable / Cover Only) */
          <div
            className={`grid gap-3 ${
              libraryFilters.displayMode === 'comfortable_grid'
                ? 'grid-cols-2 sm:grid-cols-3'
                : 'grid-cols-3 sm:grid-cols-4 md:grid-cols-5'
            }`}
          >
            {processedMangas.map(manga => {
              const unread = manga.unreadCount || 0;
              const hasTrackers = (manga.trackers && manga.trackers.length > 0) || false;

              return (
                <div
                  key={manga.id}
                  onClick={() => navigate('detail', manga.id)}
                  className="group relative bg-[#15151D] border border-[#22222E] hover:border-[#8B5CF6]/60 rounded-2xl overflow-hidden cursor-pointer flex flex-col transition-all duration-200 hover:shadow-[0_4px_20px_rgba(0,0,0,0.5)]"
                >
                  {/* Poster Image Container */}
                  <div className="relative aspect-[3/4.4] overflow-hidden bg-zinc-950">
                    <img
                      src={manga.thumbnailUrl}
                      alt={manga.title}
                      className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                      referrerPolicy="no-referrer"
                      loading="lazy"
                    />

                    {/* Gradient Overlay for badges */}
                    <div className="absolute inset-0 bg-gradient-to-t from-black/80 via-transparent to-black/40 pointer-events-none" />

                    {/* Unread Chapter Badge (Mihon Parity) */}
                    {unread > 0 && (
                      <div className="absolute top-2 right-2 px-1.5 py-0.5 rounded-lg bg-[#8B5CF6] border border-[#A78BFA]/50 text-[10px] font-bold text-white shadow-md font-mono">
                        {unread}
                      </div>
                    )}

                    {/* Source Pill Top Left */}
                    <div className="absolute top-2 left-2 px-1.5 py-0.5 rounded-md bg-black/70 backdrop-blur-sm border border-white/10 text-[9px] font-medium text-zinc-300">
                      {manga.source}
                    </div>

                    {/* Quick Resume floating button on hover */}
                    <button
                      onClick={e => handleQuickResume(e, manga)}
                      className="absolute bottom-2 right-2 w-8 h-8 rounded-full bg-[#8B5CF6] text-white flex items-center justify-center shadow-[0_0_12px_rgba(139,92,246,0.6)] opacity-0 group-hover:opacity-100 transition-all duration-200 transform scale-90 group-hover:scale-100"
                      title="Continue Reading"
                    >
                      <Play className="w-3.5 h-3.5 fill-current ml-0.5" />
                    </button>
                  </div>

                  {/* Info below artwork (shown in Compact & Comfortable modes) */}
                  {libraryFilters.displayMode !== 'cover_only' && (
                    <div className="p-2.5 flex flex-col justify-between flex-1 gap-1">
                      <h4 className="text-xs font-semibold text-zinc-100 line-clamp-2 leading-tight group-hover:text-[#A78BFA] transition-colors">
                        {manga.title}
                      </h4>
                      <div className="flex items-center justify-between text-[10px] text-zinc-400 pt-0.5 font-mono">
                        <span>{manga.latestChapter?.split(':')[0] || `Ch. ${manga.totalChapters}`}</span>
                        {manga.rating && <span className="text-amber-400 font-bold">★ {manga.rating}</span>}
                      </div>
                    </div>
                  )}
                </div>
              );
            })}
          </div>
        )}
      </main>

      {/* Filter & Sort Drawer Modal (Mihon Parity) */}
      {isFilterSheetOpen && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-end sm:items-center justify-center p-0 sm:p-4">
          <div className="w-full max-w-lg rounded-t-3xl sm:rounded-3xl bg-[#14141C] border border-[#282838] max-h-[85vh] flex flex-col shadow-2xl animate-slide-up">
            {/* Modal Header */}
            <div className="p-4 border-b border-[#242434] flex items-center justify-between">
              <div className="flex items-center gap-2">
                <SlidersHorizontal className="w-5 h-5 text-[#8B5CF6]" />
                <h3 className="text-base font-bold text-zinc-100">Filter & Sort Library</h3>
              </div>
              <div className="flex items-center gap-2">
                <button
                  onClick={resetLibraryFilters}
                  className="text-xs text-zinc-400 hover:text-zinc-200 px-2.5 py-1 rounded-lg bg-[#1D1D28]"
                >
                  Reset
                </button>
                <button
                  onClick={() => setIsFilterSheetOpen(false)}
                  className="p-1 rounded-lg text-zinc-400 hover:text-white"
                >
                  <X className="w-5 h-5" />
                </button>
              </div>
            </div>

            {/* Modal Body */}
            <div className="p-4 overflow-y-auto space-y-5 text-xs">
              {/* Display Mode Selector */}
              <div>
                <span className="font-semibold text-zinc-300 uppercase tracking-wider text-[10px]">
                  Display Mode
                </span>
                <div className="grid grid-cols-4 gap-2 mt-2">
                  {[
                    { id: 'compact_grid', label: 'Compact', icon: Grid },
                    { id: 'comfortable_grid', label: 'Comfortable', icon: LayoutGrid },
                    { id: 'list', label: 'List View', icon: List },
                    { id: 'cover_only', label: 'Cover Only', icon: BookOpen },
                  ].map(mode => {
                    const isSel = libraryFilters.displayMode === mode.id;
                    const Icon = mode.icon;
                    return (
                      <button
                        key={mode.id}
                        onClick={() => updateLibraryFilters({ displayMode: mode.id as any })}
                        className={`p-2.5 rounded-xl border flex flex-col items-center gap-1.5 transition-all ${
                          isSel
                            ? 'bg-[#8B5CF6]/20 border-[#8B5CF6] text-white shadow-sm'
                            : 'bg-[#181822] border-[#262634] text-zinc-400 hover:text-zinc-200'
                        }`}
                      >
                        <Icon className="w-4 h-4" />
                        <span className="text-[10px] font-medium">{mode.label}</span>
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* Tri-State Filters (Mihon standard) */}
              <div>
                <div className="flex items-center justify-between">
                  <span className="font-semibold text-zinc-300 uppercase tracking-wider text-[10px]">
                    Status Filters (Tap to cycle: Any → Include → Exclude)
                  </span>
                </div>
                <div className="grid grid-cols-2 gap-2 mt-2">
                  {[
                    { key: 'downloaded', label: 'Downloaded' },
                    { key: 'unread', label: 'Unread' },
                    { key: 'started', label: 'Started Reading' },
                    { key: 'completed', label: 'Completed' },
                    { key: 'tracked', label: 'Tracked (MAL/AniList)' },
                  ].map(f => {
                    const state = (libraryFilters as any)[f.key] as TriState;
                    return (
                      <button
                        key={f.key}
                        onClick={() =>
                          updateLibraryFilters({ [f.key]: nextTriState(state) })
                        }
                        className={`p-2.5 rounded-xl border flex items-center justify-between transition-all ${
                          state === 'included'
                            ? 'bg-[#8B5CF6]/20 border-[#8B5CF6] text-white'
                            : state === 'excluded'
                            ? 'bg-rose-500/20 border-rose-500/60 text-rose-300'
                            : 'bg-[#181822] border-[#262634] text-zinc-400 hover:text-zinc-200'
                        }`}
                      >
                        <span className="font-medium">{f.label}</span>
                        <span className="text-[11px] font-mono px-1.5 py-0.5 rounded">
                          {state === 'included' && <Check className="w-3.5 h-3.5 text-[#A78BFA]" />}
                          {state === 'excluded' && <X className="w-3.5 h-3.5 text-rose-400" />}
                          {state === 'none' && <Minus className="w-3.5 h-3.5 text-zinc-600" />}
                        </span>
                      </button>
                    );
                  })}
                </div>
              </div>

              {/* Sort By & Order */}
              <div>
                <span className="font-semibold text-zinc-300 uppercase tracking-wider text-[10px]">
                  Sort Criteria
                </span>
                <div className="grid grid-cols-2 sm:grid-cols-3 gap-2 mt-2">
                  {[
                    { id: 'alphabetical', label: 'Alphabetical' },
                    { id: 'lastRead', label: 'Last Read' },
                    { id: 'totalChapters', label: 'Total Chapters' },
                    { id: 'unreadCount', label: 'Unread Count' },
                    { id: 'latestUpdate', label: 'Latest Chapter' },
                    { id: 'dateAdded', label: 'Date Added' },
                  ].map(sort => {
                    const isSel = libraryFilters.sortBy === sort.id;
                    return (
                      <button
                        key={sort.id}
                        onClick={() => updateLibraryFilters({ sortBy: sort.id as any })}
                        className={`p-2.5 rounded-xl border text-left transition-all ${
                          isSel
                            ? 'bg-[#8B5CF6]/20 border-[#8B5CF6] text-white'
                            : 'bg-[#181822] border-[#262634] text-zinc-400 hover:text-zinc-200'
                        }`}
                      >
                        <span className="font-medium">{sort.label}</span>
                      </button>
                    );
                  })}
                </div>

                <div className="flex gap-2 mt-2">
                  <button
                    onClick={() => updateLibraryFilters({ sortOrder: 'asc' })}
                    className={`flex-1 py-2 rounded-xl border text-center font-medium transition-all ${
                      libraryFilters.sortOrder === 'asc'
                        ? 'bg-[#8B5CF6]/20 border-[#8B5CF6] text-white'
                        : 'bg-[#181822] border-[#262634] text-zinc-400'
                    }`}
                  >
                    Ascending (A → Z)
                  </button>
                  <button
                    onClick={() => updateLibraryFilters({ sortOrder: 'desc' })}
                    className={`flex-1 py-2 rounded-xl border text-center font-medium transition-all ${
                      libraryFilters.sortOrder === 'desc'
                        ? 'bg-[#8B5CF6]/20 border-[#8B5CF6] text-white'
                        : 'bg-[#181822] border-[#262634] text-zinc-400'
                    }`}
                  >
                    Descending (Z → A)
                  </button>
                </div>
              </div>
            </div>

            {/* Modal Footer */}
            <div className="p-4 border-t border-[#242434] bg-[#101018]">
              <button
                onClick={() => setIsFilterSheetOpen(false)}
                className="w-full py-2.5 rounded-xl bg-[#8B5CF6] hover:bg-[#7C3AED] text-white font-bold transition-colors shadow-lg"
              >
                Apply Filters ({processedMangas.length} results)
              </button>
            </div>
          </div>
        </div>
      )}

      {/* Category Management Modal */}
      {isCategoryModalOpen && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="w-full max-w-sm rounded-3xl bg-[#14141C] border border-[#282838] p-5 shadow-2xl space-y-4 animate-scale-in">
            <div className="flex items-center justify-between">
              <div className="flex items-center gap-2">
                <FolderPlus className="w-5 h-5 text-[#8B5CF6]" />
                <h3 className="text-base font-bold text-zinc-100">Manage Categories</h3>
              </div>
              <button
                onClick={() => setIsCategoryModalOpen(false)}
                className="p-1 text-zinc-400 hover:text-white"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            {/* Add New Category */}
            <div className="flex gap-2">
              <input
                type="text"
                value={newCatName}
                onChange={e => setNewCatName(e.target.value)}
                placeholder="New category name..."
                className="flex-1 px-3 py-2 bg-[#1A1A24] border border-[#2C2C3E] rounded-xl text-xs text-white focus:outline-none focus:border-[#8B5CF6]"
              />
              <button
                onClick={() => {
                  if (newCatName.trim()) {
                    addCategory(newCatName.trim());
                    setNewCatName('');
                  }
                }}
                className="px-3.5 py-2 rounded-xl bg-[#8B5CF6] text-white text-xs font-bold hover:bg-[#7C3AED]"
              >
                Add
              </button>
            </div>

            {/* Existing Categories List */}
            <div className="space-y-1.5 max-h-56 overflow-y-auto pr-1">
              {categories.map(cat => (
                <div
                  key={cat.id}
                  className="flex items-center justify-between p-2.5 rounded-xl bg-[#191924] border border-[#272738] text-xs"
                >
                  <span className="font-medium text-zinc-200">{cat.name}</span>
                  {!cat.isDefault && (
                    <button
                      onClick={() => removeCategory(cat.id)}
                      className="text-zinc-500 hover:text-rose-400 p-1"
                    >
                      <Trash2 className="w-3.5 h-3.5" />
                    </button>
                  )}
                </div>
              ))}
            </div>

            <button
              onClick={() => setIsCategoryModalOpen(false)}
              className="w-full py-2 rounded-xl bg-[#222230] text-zinc-300 text-xs font-semibold hover:bg-[#2C2C3E]"
            >
              Done
            </button>
          </div>
        </div>
      )}

      {/* Quick Move Manga to Category Modal */}
      {managingMangaId !== null && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="w-full max-w-xs rounded-2xl bg-[#14141C] border border-[#282838] p-5 shadow-2xl space-y-3">
            <h4 className="text-sm font-bold text-zinc-100">Set Category</h4>
            <div className="space-y-1.5">
              {['Reading', 'Completed', 'Plan to Read', 'On Hold', 'Dropped'].map(catName => (
                <button
                  key={catName}
                  onClick={() => {
                    setMangaCategory(managingMangaId, catName);
                    setManagingMangaId(null);
                  }}
                  className="w-full p-2.5 rounded-xl bg-[#1A1A26] hover:bg-[#8B5CF6]/20 border border-[#2A2A3C] hover:border-[#8B5CF6] text-left text-xs font-medium text-zinc-200 transition-colors"
                >
                  {catName}
                </button>
              ))}
            </div>
            <button
              onClick={() => setManagingMangaId(null)}
              className="w-full py-2 rounded-xl bg-[#222230] text-zinc-400 text-xs font-medium"
            >
              Cancel
            </button>
          </div>
        </div>
      )}
    </div>
  );
};
