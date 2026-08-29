import React, { useState } from 'react';
import {
  ArrowLeft,
  Bookmark,
  BookmarkCheck,
  Download,
  Share2,
  BookOpen,
  Compass,
  Sparkles,
  GitBranch,
  CheckCircle2,
  Play,
  ArrowUpDown,
  Search,
  Check,
  ExternalLink,
  RefreshCw,
  FolderPlus,
  Star,
  Layers,
  ChevronDown,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { MangaTracker } from '../../types';

interface MangaDetailScreenProps {
  mangaId: number;
}

export const MangaDetailScreen: React.FC<MangaDetailScreenProps> = ({ mangaId }) => {
  const {
    getManga,
    getChaptersForManga,
    toggleLibrary,
    toggleChapterDownload,
    downloadAllForManga,
    markChapterRead,
    navigate,
    goBack,
    snaps,
    categories,
    setMangaCategory,
    updateMangaTracker,
    addMangaTracker,
    removeMangaTracker,
    showToast,
  } = useLumina();

  const manga = getManga(mangaId) || getManga(1)!;
  const chapters = getChaptersForManga(manga.id);
  const [isDescExpanded, setIsDescExpanded] = useState(false);
  const [searchChapter, setSearchChapter] = useState('');
  const [sortAsc, setSortAsc] = useState(false);
  const [activeTab, setActiveTab] = useState<'chapters' | 'tracking' | 'lore'>('chapters');
  const [isTrackerModalOpen, setIsTrackerModalOpen] = useState(false);
  const [selectedService, setSelectedService] = useState<'anilist' | 'myanimelist' | 'kitsu'>('anilist');
  const [trackingStatus, setTrackingStatus] = useState<string>('READING');
  const [trackingScore, setTrackingScore] = useState<number>(9.0);
  const [trackingChapter, setTrackingChapter] = useState<number>(19);

  const existingSnap = snaps.find(s => s.mangaId === manga.id);

  const filteredChapters = chapters
    .filter(c => c.title.toLowerCase().includes(searchChapter.toLowerCase()) || c.chapterNumber.toString().includes(searchChapter))
    .sort((a, b) => (sortAsc ? a.chapterNumber - b.chapterNumber : b.chapterNumber - a.chapterNumber));

  const trackers = manga.trackers || [];

  const handleSyncTracker = (tracker: MangaTracker) => {
    showToast(`Synced ${tracker.serviceName} to Ch. ${manga.totalChapters}`);
    updateMangaTracker(manga.id, tracker.service, {
      lastChapterRead: manga.totalChapters,
      status: manga.status === 'Completed' ? 'COMPLETED' : 'READING',
    });
  };

  const handleSaveTrackerModal = () => {
    const serviceName = selectedService === 'anilist' ? 'AniList' : selectedService === 'myanimelist' ? 'MyAnimeList' : 'Kitsu';
    const color = selectedService === 'anilist' ? '#02A9FF' : selectedService === 'myanimelist' ? '#2E51A2' : '#FD755C';
    
    addMangaTracker(manga.id, {
      service: selectedService,
      serviceName,
      color,
      isConnected: true,
      trackingId: `${manga.id * 1234}`,
      title: manga.title,
      status: trackingStatus as any,
      score: trackingScore,
      lastChapterRead: trackingChapter,
      totalChapters: manga.totalChapters,
    });
    setIsTrackerModalOpen(false);
  };

  return (
    <div className="min-h-screen bg-[#0C0C10] text-white pb-24 selection:bg-[#8B5CF6]/30">
      {/* Immersive Banner Cover */}
      <div className="relative h-64 sm:h-72 w-full overflow-hidden bg-zinc-950">
        <img
          src={manga.bannerUrl || manga.thumbnailUrl}
          alt={manga.title}
          className="w-full h-full object-cover opacity-50 scale-105 filter blur-[1px]"
          referrerPolicy="no-referrer"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-[#0C0C10] via-[#0C0C10]/70 to-transparent" />

        {/* Top bar controls */}
        <div className="absolute top-4 left-4 right-4 flex items-center justify-between z-10">
          <button
            onClick={goBack}
            className="w-10 h-10 rounded-full bg-black/60 backdrop-blur-md border border-[#272733] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div className="flex items-center gap-2">
            <button
              onClick={() => toggleLibrary(manga.id)}
              className="w-10 h-10 rounded-full bg-black/60 backdrop-blur-md border border-[#272733] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
              title={manga.inLibrary ? 'In Library' : 'Add to Library'}
            >
              {manga.inLibrary ? (
                <BookmarkCheck className="w-5 h-5 text-[#00E5FF]" />
              ) : (
                <Bookmark className="w-5 h-5" />
              )}
            </button>
            <button
              onClick={() => {
                if (navigator.share) {
                  navigator.share({ title: manga.title, text: `Read ${manga.title} on Lumina Reader` });
                } else {
                  showToast('Share link copied to clipboard');
                }
              }}
              className="w-10 h-10 rounded-full bg-black/60 backdrop-blur-md border border-[#272733] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
            >
              <Share2 className="w-4 h-4" />
            </button>
          </div>
        </div>

        {/* Poster & Title Header */}
        <div className="absolute bottom-4 left-4 right-4 flex gap-4 items-end">
          <div className="relative w-20 h-28 sm:w-24 sm:h-34 rounded-xl overflow-hidden border-2 border-[#2E2E3E] shadow-2xl flex-shrink-0 bg-zinc-900">
            <img
              src={manga.thumbnailUrl}
              alt={manga.title}
              className="w-full h-full object-cover"
              referrerPolicy="no-referrer"
            />
          </div>
          <div className="min-w-0 flex-1 pb-1">
            <div className="flex items-center gap-2 flex-wrap">
              <span className="px-2 py-0.5 rounded-md bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 text-[#A78BFA] text-[10px] font-bold uppercase">
                {manga.status}
              </span>
              <span className="px-2 py-0.5 rounded-md bg-[#00E5FF]/10 border border-[#00E5FF]/30 text-[#00E5FF] text-[10px] font-semibold">
                {manga.source}
              </span>
            </div>
            <h1 className="text-lg sm:text-xl font-black text-white mt-1.5 line-clamp-2 leading-tight">
              {manga.title}
            </h1>
            <p className="text-xs text-zinc-300 font-medium mt-0.5">{manga.author}</p>
          </div>
        </div>
      </div>

      <main className="max-w-xl mx-auto px-4 pt-3 space-y-4">
        {/* Genre Tags & Category Selector */}
        <div className="flex items-center justify-between flex-wrap gap-2">
          <div className="flex flex-wrap gap-1.5">
            {manga.genre.map(g => (
              <span
                key={g}
                className="px-2.5 py-1 rounded-lg bg-[#14141C] border border-[#242432] text-[11px] font-semibold text-zinc-300"
              >
                {g}
              </span>
            ))}
            <span className="px-2.5 py-1 rounded-lg bg-[#14141C] border border-amber-500/30 text-[11px] font-bold text-amber-400">
              ★ {manga.rating}
            </span>
          </div>

          {/* Category Selector Pill */}
          <div className="relative">
            <select
              value={manga.category || 'Reading'}
              onChange={e => setMangaCategory(manga.id, e.target.value)}
              className="bg-[#14141C] border border-[#262638] text-xs text-[#A78BFA] font-semibold rounded-xl px-3 py-1.5 outline-none cursor-pointer hover:border-[#8B5CF6]"
            >
              {categories.filter(c => c.name !== 'All').map(c => (
                <option key={c.id} value={c.name} className="bg-[#14141C] text-white">
                  Category: {c.name}
                </option>
              ))}
            </select>
          </div>
        </div>

        {/* Description */}
        <div className="bg-[#14141C] border border-[#242432] rounded-2xl p-4 space-y-2">
          <p className={`text-xs text-zinc-300 leading-relaxed ${isDescExpanded ? '' : 'line-clamp-3'}`}>
            {manga.description}
          </p>
          <button
            onClick={() => setIsDescExpanded(!isDescExpanded)}
            className="text-[11px] font-bold text-[#A78BFA] hover:underline"
          >
            {isDescExpanded ? 'Show less' : 'Read more'}
          </button>
        </div>

        {/* Primary CTA Buttons */}
        <div className="flex gap-2">
          <button
            onClick={() => {
              const chId = existingSnap ? existingSnap.chapterId : chapters[0]?.id || 101;
              const pIdx = existingSnap ? existingSnap.pageIndex : 0;
              navigate('reader', manga.id, chId, pIdx);
            }}
            className="flex-1 py-3 px-4 rounded-xl bg-gradient-to-r from-[#8B5CF6] to-[#6366F1] hover:from-[#7C3AED] hover:to-[#4F46E5] text-white font-bold text-xs sm:text-sm shadow-[0_0_16px_rgba(139,92,246,0.4)] flex items-center justify-center gap-2 transition-all active:scale-[0.98]"
          >
            <Play className="w-4 h-4 fill-white" />
            {existingSnap ? `Resume (Ch. ${existingSnap.chapterNumber})` : 'Start Reading'}
          </button>

          <button
            onClick={() => toggleLibrary(manga.id)}
            className={`px-4 py-3 rounded-xl border font-bold text-xs flex items-center gap-2 transition-colors ${
              manga.inLibrary
                ? 'bg-[#00E5FF]/10 border-[#00E5FF]/40 text-[#00E5FF]'
                : 'bg-[#14141C] border-[#242432] text-white hover:border-[#8B5CF6]'
            }`}
          >
            {manga.inLibrary ? <BookmarkCheck className="w-4 h-4" /> : <Bookmark className="w-4 h-4" />}
            {manga.inLibrary ? 'In Library' : 'Add'}
          </button>

          <button
            onClick={() => downloadAllForManga(manga.id)}
            className="px-3.5 py-3 rounded-xl bg-[#14141C] border border-[#242432] hover:border-[#00E5FF] text-zinc-300 hover:text-white transition-colors"
            title="Download All Chapters"
          >
            <Download className="w-4 h-4" />
          </button>
        </div>

        {/* Navigation Tabs: Chapters / Tracking / Lore */}
        <div className="flex border-b border-[#242434] pt-2">
          {[
            { id: 'chapters', label: `Chapters (${chapters.length})`, icon: BookOpen },
            { id: 'tracking', label: `Tracking (${trackers.length})`, icon: Star },
            { id: 'lore', label: 'Lore & Nodes', icon: Compass },
          ].map(tab => {
            const isSel = activeTab === tab.id;
            const Icon = tab.icon;
            return (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id as any)}
                className={`flex-1 pb-3 text-xs font-bold flex items-center justify-center gap-2 border-b-2 transition-all ${
                  isSel
                    ? 'border-[#8B5CF6] text-white'
                    : 'border-transparent text-zinc-400 hover:text-zinc-200'
                }`}
              >
                <Icon className="w-4 h-4" />
                {tab.label}
              </button>
            );
          })}
        </div>

        {/* TAB 1: CHAPTERS LIST */}
        {activeTab === 'chapters' && (
          <div className="space-y-3">
            <div className="flex items-center justify-between gap-2">
              <div className="relative flex-1">
                <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-zinc-500" />
                <input
                  type="text"
                  value={searchChapter}
                  onChange={e => setSearchChapter(e.target.value)}
                  placeholder="Search chapter number or title..."
                  className="w-full bg-[#14141C] border border-[#242432] rounded-xl pl-9 pr-3 py-2 text-xs text-white placeholder-zinc-500 outline-none"
                />
              </div>

              <button
                onClick={() => setSortAsc(!sortAsc)}
                className="text-xs text-zinc-400 hover:text-white flex items-center gap-1 bg-[#14141C] border border-[#242432] px-3 py-2 rounded-xl"
              >
                <ArrowUpDown className="w-3 h-3" />
                {sortAsc ? 'Oldest' : 'Newest'}
              </button>
            </div>

            {/* Chapters List */}
            <div className="space-y-2">
              {filteredChapters.map(ch => (
                <div
                  key={ch.id}
                  onClick={() => navigate('reader', manga.id, ch.id, 0)}
                  className={`bg-[#14141C] hover:bg-[#1A1A24] border rounded-xl p-3 flex items-center justify-between gap-3 cursor-pointer transition-all ${
                    ch.isRead ? 'border-[#242432] opacity-75' : 'border-[#242432] hover:border-[#8B5CF6]/50'
                  }`}
                >
                  <div className="min-w-0 flex-1">
                    <div className="flex items-center gap-2">
                      <span className="text-xs font-bold text-white">Ch. {ch.chapterNumber}</span>
                      {ch.title && (
                        <span className="text-xs text-zinc-300 truncate font-medium">
                          • {ch.title}
                        </span>
                      )}
                    </div>
                    <p className="text-[10px] text-zinc-400 mt-0.5">
                      {ch.dateUpload} • {ch.scanlator} • {ch.pageCount} Pages
                    </p>
                  </div>

                  <div className="flex items-center gap-2">
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        markChapterRead(ch.id, !ch.isRead);
                      }}
                      className={`p-1.5 rounded-lg text-xs ${
                        ch.isRead ? 'text-emerald-400 bg-emerald-950/30' : 'text-zinc-500 hover:text-zinc-300'
                      }`}
                      title={ch.isRead ? 'Mark as Unread' : 'Mark as Read'}
                    >
                      <CheckCircle2 className="w-4 h-4" />
                    </button>

                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        toggleChapterDownload(ch.id);
                      }}
                      className={`p-1.5 rounded-lg text-xs ${
                        ch.isDownloaded ? 'text-[#00E5FF] bg-[#00E5FF]/10' : 'text-zinc-500 hover:text-zinc-300'
                      }`}
                      title={ch.isDownloaded ? 'Downloaded' : 'Download Chapter'}
                    >
                      <Download className="w-4 h-4" />
                    </button>
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* TAB 2: TRACKERS (Mihon Parity + Enhancements) */}
        {activeTab === 'tracking' && (
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <div>
                <h4 className="text-xs font-bold text-zinc-200">External Tracking Services</h4>
                <p className="text-[11px] text-zinc-400">Sync progress with AniList, MyAnimeList & Kitsu</p>
              </div>
              <button
                onClick={() => setIsTrackerModalOpen(true)}
                className="px-3 py-1.5 rounded-xl bg-[#8B5CF6] text-white text-xs font-semibold hover:bg-[#7C3AED] transition-colors shadow-sm"
              >
                + Add Tracker
              </button>
            </div>

            {trackers.length === 0 ? (
              <div className="py-12 text-center rounded-2xl bg-[#14141C] border border-[#242432] p-4 space-y-3">
                <Star className="w-8 h-8 text-zinc-500 mx-auto" />
                <p className="text-xs text-zinc-400">No trackers connected yet.</p>
                <button
                  onClick={() => setIsTrackerModalOpen(true)}
                  className="px-4 py-2 rounded-xl bg-[#1F1F2C] border border-[#2E2E40] text-zinc-200 text-xs font-semibold hover:border-[#8B5CF6]"
                >
                  Connect AniList / MyAnimeList
                </button>
              </div>
            ) : (
              <div className="space-y-2.5">
                {trackers.map(tracker => (
                  <div
                    key={tracker.service}
                    className="p-3.5 rounded-2xl bg-[#14141C] border border-[#262636] space-y-3"
                  >
                    <div className="flex items-center justify-between">
                      <div className="flex items-center gap-2.5">
                        <div
                          className="w-8 h-8 rounded-xl flex items-center justify-center font-bold text-white text-xs shadow-md"
                          style={{ backgroundColor: tracker.color || '#8B5CF6' }}
                        >
                          {tracker.serviceName[0]}
                        </div>
                        <div>
                          <h5 className="text-xs font-bold text-white">{tracker.serviceName}</h5>
                          <span className="text-[10px] text-emerald-400 font-medium">● Connected</span>
                        </div>
                      </div>

                      <div className="flex items-center gap-1.5">
                        <button
                          onClick={() => handleSyncTracker(tracker)}
                          className="p-2 rounded-xl bg-[#1F1F2C] hover:bg-[#8B5CF6]/20 text-zinc-300 hover:text-[#A78BFA] transition-colors"
                          title="Sync Progress Now"
                        >
                          <RefreshCw className="w-3.5 h-3.5" />
                        </button>
                        <button
                          onClick={() => removeMangaTracker(manga.id, tracker.service)}
                          className="text-[11px] text-zinc-500 hover:text-rose-400 px-2 py-1"
                        >
                          Unlink
                        </button>
                      </div>
                    </div>

                    <div className="grid grid-cols-3 gap-2 pt-1 text-center">
                      <div className="p-2 rounded-xl bg-[#191924] border border-[#272736]">
                        <span className="text-[10px] text-zinc-400 block">Status</span>
                        <span className="text-xs font-bold text-[#A78BFA]">{tracker.status}</span>
                      </div>
                      <div className="p-2 rounded-xl bg-[#191924] border border-[#272736]">
                        <span className="text-[10px] text-zinc-400 block">Score</span>
                        <span className="text-xs font-bold text-amber-400">★ {tracker.score || '-'}/10</span>
                      </div>
                      <div className="p-2 rounded-xl bg-[#191924] border border-[#272736]">
                        <span className="text-[10px] text-zinc-400 block">Chapters Read</span>
                        <span className="text-xs font-bold text-white">
                          {tracker.lastChapterRead || 0}/{tracker.totalChapters || manga.totalChapters}
                        </span>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            )}
          </div>
        )}

        {/* TAB 3: LORE & NODES */}
        {activeTab === 'lore' && (
          <div className="space-y-2">
            <span className="text-[11px] font-extrabold uppercase tracking-wider text-[#00E5FF]">
              Narrative Intelligence
            </span>

            <div className="grid grid-cols-3 gap-2">
              <button
                onClick={() => navigate('universe', manga.id)}
                className="bg-[#14141C] hover:bg-[#1A1A26] border border-[#242432] hover:border-[#00E5FF]/50 rounded-xl p-3 flex flex-col items-center text-center gap-1.5 transition-colors group"
              >
                <Compass className="w-5 h-5 text-[#00E5FF] group-hover:scale-110 transition-transform" />
                <span className="text-[11px] font-bold text-white">Universe Map</span>
                <span className="text-[9px] text-zinc-400">Character Nodes</span>
              </button>

              <button
                onClick={() => navigate('dna')}
                className="bg-[#14141C] hover:bg-[#1A1A26] border border-[#242432] hover:border-[#8B5CF6]/50 rounded-xl p-3 flex flex-col items-center text-center gap-1.5 transition-colors group"
              >
                <Sparkles className="w-5 h-5 text-[#A78BFA] group-hover:scale-110 transition-transform" />
                <span className="text-[11px] font-bold text-white">Manga DNA</span>
                <span className="text-[9px] text-zinc-400">Genome Profile</span>
              </button>

              <button
                onClick={() => navigate('timeline', manga.id)}
                className="bg-[#14141C] hover:bg-[#1A1A26] border border-[#242432] hover:border-amber-500/50 rounded-xl p-3 flex flex-col items-center text-center gap-1.5 transition-colors group"
              >
                <GitBranch className="w-5 h-5 text-amber-400 group-hover:scale-110 transition-transform" />
                <span className="text-[11px] font-bold text-white">Timeline</span>
                <span className="text-[9px] text-zinc-400">Story Arcs</span>
              </button>
            </div>
          </div>
        )}
      </main>

      {/* Tracker Connect / Edit Modal */}
      {isTrackerModalOpen && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="w-full max-w-sm rounded-3xl bg-[#14141C] border border-[#282838] p-5 shadow-2xl space-y-4 animate-scale-in">
            <h4 className="text-base font-bold text-zinc-100">Connect Tracker</h4>

            {/* Service select */}
            <div>
              <span className="text-[10px] uppercase font-semibold text-zinc-400">Service</span>
              <div className="grid grid-cols-3 gap-2 mt-1">
                {[
                  { id: 'anilist', label: 'AniList' },
                  { id: 'myanimelist', label: 'MyAnimeList' },
                  { id: 'kitsu', label: 'Kitsu' },
                ].map(srv => (
                  <button
                    key={srv.id}
                    onClick={() => setSelectedService(srv.id as any)}
                    className={`py-2 rounded-xl text-xs font-semibold border transition-all ${
                      selectedService === srv.id
                        ? 'bg-[#8B5CF6]/20 border-[#8B5CF6] text-white'
                        : 'bg-[#181824] border-[#272738] text-zinc-400'
                    }`}
                  >
                    {srv.label}
                  </button>
                ))}
              </div>
            </div>

            {/* Status select */}
            <div>
              <span className="text-[10px] uppercase font-semibold text-zinc-400">Status</span>
              <select
                value={trackingStatus}
                onChange={e => setTrackingStatus(e.target.value)}
                className="w-full mt-1 bg-[#181824] border border-[#272738] rounded-xl px-3 py-2 text-xs text-white outline-none"
              >
                <option value="READING">Reading</option>
                <option value="COMPLETED">Completed</option>
                <option value="ON_HOLD">On Hold</option>
                <option value="DROPPED">Dropped</option>
                <option value="PLAN_TO_READ">Plan to Read</option>
              </select>
            </div>

            {/* Score & Chapters */}
            <div className="grid grid-cols-2 gap-2">
              <div>
                <span className="text-[10px] uppercase font-semibold text-zinc-400">Score (1-10)</span>
                <input
                  type="number"
                  step="0.5"
                  min="0"
                  max="10"
                  value={trackingScore}
                  onChange={e => setTrackingScore(parseFloat(e.target.value) || 0)}
                  className="w-full mt-1 bg-[#181824] border border-[#272738] rounded-xl px-3 py-2 text-xs text-white outline-none font-mono"
                />
              </div>
              <div>
                <span className="text-[10px] uppercase font-semibold text-zinc-400">Last Read Ch.</span>
                <input
                  type="number"
                  min="0"
                  max={manga.totalChapters}
                  value={trackingChapter}
                  onChange={e => setTrackingChapter(parseInt(e.target.value) || 0)}
                  className="w-full mt-1 bg-[#181824] border border-[#272738] rounded-xl px-3 py-2 text-xs text-white outline-none font-mono"
                />
              </div>
            </div>

            <div className="flex gap-2 pt-2">
              <button
                onClick={() => setIsTrackerModalOpen(false)}
                className="flex-1 py-2 rounded-xl bg-[#222230] text-zinc-300 text-xs font-semibold"
              >
                Cancel
              </button>
              <button
                onClick={handleSaveTrackerModal}
                className="flex-1 py-2 rounded-xl bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-semibold shadow-md"
              >
                Save Tracker
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
