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
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';

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
  } = useLumina();

  const manga = getManga(mangaId) || getManga(1)!;
  const chapters = getChaptersForManga(manga.id);
  const [isDescExpanded, setIsDescExpanded] = useState(false);
  const [searchChapter, setSearchChapter] = useState('');
  const [sortAsc, setSortAsc] = useState(false);

  const existingSnap = snaps.find(s => s.mangaId === manga.id);

  const filteredChapters = chapters
    .filter(c => c.title.toLowerCase().includes(searchChapter.toLowerCase()) || c.chapterNumber.toString().includes(searchChapter))
    .sort((a, b) => (sortAsc ? a.chapterNumber - b.chapterNumber : b.chapterNumber - a.chapterNumber));

  return (
    <div className="min-h-screen bg-black text-white pb-24">
      {/* Immersive Banner Cover */}
      <div className="relative h-64 sm:h-72 w-full overflow-hidden">
        <img
          src={manga.bannerUrl || manga.thumbnailUrl}
          alt={manga.title}
          className="w-full h-full object-cover opacity-60 scale-105"
        />
        <div className="absolute inset-0 bg-gradient-to-t from-black via-black/60 to-transparent" />

        {/* Top bar controls */}
        <div className="absolute top-4 left-4 right-4 flex items-center justify-between z-10">
          <button
            onClick={goBack}
            className="w-10 h-10 rounded-full bg-black/60 backdrop-blur-md border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
          >
            <ArrowLeft className="w-5 h-5" />
          </button>
          <div className="flex items-center gap-2">
            <button
              onClick={() => toggleLibrary(manga.id)}
              className="w-10 h-10 rounded-full bg-black/60 backdrop-blur-md border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
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
                }
              }}
              className="w-10 h-10 rounded-full bg-black/60 backdrop-blur-md border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
            >
              <Share2 className="w-4 h-4" />
            </button>
          </div>
        </div>

        {/* Poster Header */}
        <div className="absolute bottom-4 left-4 right-4 flex gap-4 items-end">
          <img
            src={manga.thumbnailUrl}
            alt={manga.title}
            className="w-20 h-28 sm:w-24 sm:h-34 object-cover rounded-xl border-2 border-[#27272A] shadow-2xl flex-shrink-0"
          />
          <div className="min-w-0 flex-1 pb-1">
            <span className="px-2 py-0.5 rounded bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 text-[#A78BFA] text-[10px] font-bold uppercase">
              {manga.status} • {manga.source}
            </span>
            <h1 className="text-lg sm:text-xl font-black text-white mt-1 line-clamp-2 leading-tight">
              {manga.title}
            </h1>
            <p className="text-xs text-zinc-300 font-medium mt-0.5">{manga.author}</p>
          </div>
        </div>
      </div>

      <main className="max-w-xl mx-auto px-4 pt-3 space-y-5">
        {/* Genre Tags */}
        <div className="flex flex-wrap gap-1.5">
          {manga.genre.map(g => (
            <span
              key={g}
              className="px-2.5 py-1 rounded-lg bg-[#121216] border border-[#27272A] text-xs font-semibold text-zinc-300"
            >
              {g}
            </span>
          ))}
          <span className="px-2.5 py-1 rounded-lg bg-[#121216] border border-amber-500/30 text-xs font-bold text-amber-400">
            ★ {manga.rating}
          </span>
        </div>

        {/* Description */}
        <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-2">
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
            className="flex-1 py-3 px-4 rounded-xl bg-gradient-to-r from-[#8B5CF6] to-[#7C3AED] hover:from-[#7C3AED] hover:to-[#6D28D9] text-white font-bold text-sm shadow-[0_0_16px_rgba(139,92,246,0.4)] flex items-center justify-center gap-2 transition-all"
          >
            <Play className="w-4 h-4 fill-white" />
            {existingSnap ? `Resume (Ch. ${existingSnap.chapterNumber})` : 'Start Reading'}
          </button>

          <button
            onClick={() => toggleLibrary(manga.id)}
            className={`px-4 py-3 rounded-xl border font-bold text-xs flex items-center gap-2 transition-colors ${
              manga.inLibrary
                ? 'bg-[#00E5FF]/10 border-[#00E5FF]/40 text-[#00E5FF]'
                : 'bg-[#121216] border-[#27272A] text-white hover:border-[#8B5CF6]'
            }`}
          >
            {manga.inLibrary ? <BookmarkCheck className="w-4 h-4" /> : <Bookmark className="w-4 h-4" />}
            {manga.inLibrary ? 'In Library' : 'Add'}
          </button>

          <button
            onClick={() => downloadAllForManga(manga.id)}
            className="px-3.5 py-3 rounded-xl bg-[#121216] border border-[#27272A] hover:border-[#00E5FF] text-zinc-300 hover:text-white"
            title="Download All Chapters"
          >
            <Download className="w-4 h-4" />
          </button>
        </div>

        {/* ECOSYSTEM LORE & INTELLIGENCE ENTRY POINTS */}
        <div className="space-y-2">
          <span className="text-[11px] font-extrabold uppercase tracking-wider text-[#00E5FF]">
            Narrative Modules & Lore
          </span>

          <div className="grid grid-cols-3 gap-2">
            <button
              onClick={() => navigate('universe', manga.id)}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#00E5FF]/50 rounded-xl p-3 flex flex-col items-center text-center gap-1.5 transition-colors group"
            >
              <Compass className="w-5 h-5 text-[#00E5FF] group-hover:scale-110 transition-transform" />
              <span className="text-[11px] font-bold text-white">Universe Map</span>
              <span className="text-[9px] text-zinc-400">Character Nodes</span>
            </button>

            <button
              onClick={() => navigate('dna')}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-xl p-3 flex flex-col items-center text-center gap-1.5 transition-colors group"
            >
              <Sparkles className="w-5 h-5 text-[#A78BFA] group-hover:scale-110 transition-transform" />
              <span className="text-[11px] font-bold text-white">Manga DNA</span>
              <span className="text-[9px] text-zinc-400">Genome Profile</span>
            </button>

            <button
              onClick={() => navigate('timeline', manga.id)}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-amber-500/50 rounded-xl p-3 flex flex-col items-center text-center gap-1.5 transition-colors group"
            >
              <GitBranch className="w-5 h-5 text-amber-400 group-hover:scale-110 transition-transform" />
              <span className="text-[11px] font-bold text-white">Timeline</span>
              <span className="text-[9px] text-zinc-400">Story Arcs</span>
            </button>
          </div>
        </div>

        {/* CHAPTERS SECTION */}
        <div className="space-y-3 pt-2">
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-bold text-white flex items-center gap-2">
              <BookOpen className="w-4 h-4 text-[#A78BFA]" />
              Chapters ({chapters.length})
            </h3>
            <button
              onClick={() => setSortAsc(!sortAsc)}
              className="text-xs text-zinc-400 hover:text-white flex items-center gap-1 bg-[#121216] border border-[#27272A] px-2.5 py-1 rounded-lg"
            >
              <ArrowUpDown className="w-3 h-3" />
              {sortAsc ? 'Oldest' : 'Newest'}
            </button>
          </div>

          <div className="relative">
            <Search className="absolute left-3 top-1/2 -translate-y-1/2 w-3.5 h-3.5 text-zinc-500" />
            <input
              type="text"
              value={searchChapter}
              onChange={e => setSearchChapter(e.target.value)}
              placeholder="Search chapter title or number..."
              className="w-full bg-[#121216] border border-[#27272A] rounded-xl pl-9 pr-3 py-1.5 text-xs text-white placeholder-zinc-500 outline-none"
            />
          </div>

          {/* Chapters List */}
          <div className="space-y-2">
            {filteredChapters.map(ch => (
              <div
                key={ch.id}
                onClick={() => navigate('reader', manga.id, ch.id, 0)}
                className={`bg-[#121216] hover:bg-[#18181E] border rounded-xl p-3 flex items-center justify-between gap-3 cursor-pointer transition-all ${
                  ch.isRead ? 'border-[#27272A] opacity-75' : 'border-[#27272A] hover:border-[#8B5CF6]/50'
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
                  <p className="text-[10px] text-zinc-500 mt-0.5">
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
      </main>
    </div>
  );
};
