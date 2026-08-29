import React from 'react';
import {
  Flame,
  Zap,
  BookOpen,
  Sparkles,
  ChevronRight,
  TrendingUp,
  Compass,
  DownloadCloud,
  CheckCircle2,
  Clock,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { HeaderBar } from '../navigation/HeaderBar';

export const HomeDashboardScreen: React.FC = () => {
  const {
    mangas,
    snaps,
    navigate,
    restoreSnap,
    readingStreakDays,
    readingSpeedPpm,
    updates,
  } = useLumina();

  const heroManga = mangas[0]; // Bleach TYBW
  const latestSnap = snaps[0];
  const trendingMangas = mangas.slice(1, 5);

  return (
    <div className="min-h-screen bg-black text-white pb-24">
      <HeaderBar />

      <main className="max-w-xl mx-auto px-4 pt-3 space-y-6">
        {/* DAILY STATS STRIP */}
        <div className="grid grid-cols-3 gap-2.5">
          <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-3 flex items-center gap-2.5">
            <div className="w-8 h-8 rounded-xl bg-amber-500/10 border border-amber-500/30 flex items-center justify-center text-amber-400">
              <Flame className="w-4 h-4 fill-amber-400" />
            </div>
            <div>
              <div className="text-xs text-zinc-400 font-medium">Daily Streak</div>
              <div className="text-sm font-bold text-white">{readingStreakDays} Days 🔥</div>
            </div>
          </div>

          <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-3 flex items-center gap-2.5">
            <div className="w-8 h-8 rounded-xl bg-[#00E5FF]/10 border border-[#00E5FF]/30 flex items-center justify-center text-[#00E5FF]">
              <Zap className="w-4 h-4 fill-[#00E5FF]" />
            </div>
            <div>
              <div className="text-xs text-zinc-400 font-medium">Velocity</div>
              <div className="text-sm font-bold text-white">{readingSpeedPpm} PPM</div>
            </div>
          </div>

          <div
            onClick={() => navigate('dna')}
            className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-2xl p-3 flex items-center gap-2.5 cursor-pointer transition-colors"
          >
            <div className="w-8 h-8 rounded-xl bg-[#8B5CF6]/10 border border-[#8B5CF6]/30 flex items-center justify-center text-[#A78BFA]">
              <Sparkles className="w-4 h-4" />
            </div>
            <div>
              <div className="text-xs text-zinc-400 font-medium">Manga DNA</div>
              <div className="text-sm font-bold text-[#A78BFA]">Level 4</div>
            </div>
          </div>
        </div>

        {/* HERO CONTINUE READING CARD (Matching Flagship Lumina Noir Experience) */}
        {latestSnap ? (
          <div className="relative rounded-2xl overflow-hidden border border-[#8B5CF6]/30 bg-[#09090B] shadow-[0_0_24px_rgba(139,92,246,0.15)] group">
            <div
              className="absolute inset-0 bg-cover bg-center opacity-30 group-hover:opacity-40 transition-opacity blur-sm scale-105"
              style={{ backgroundImage: `url(${latestSnap.coverUrl})` }}
            />
            <div className="absolute inset-0 bg-gradient-to-t from-black via-black/80 to-transparent" />

            <div className="relative p-5 space-y-4">
              <div className="flex items-center justify-between">
                <span className="inline-flex items-center gap-1.5 px-2.5 py-1 rounded-full bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 text-[#A78BFA] text-xs font-bold uppercase tracking-wider">
                  <BookOpen className="w-3 h-3" />
                  Continue Reading
                </span>
                <span className="text-xs text-zinc-400 flex items-center gap-1">
                  <Clock className="w-3 h-3" />
                  Snap Restored
                </span>
              </div>

              <div className="flex gap-4 items-center">
                <img
                  src={latestSnap.coverUrl}
                  alt={latestSnap.title}
                  className="w-16 h-22 object-cover rounded-xl border border-[#27272A] shadow-lg flex-shrink-0"
                />
                <div className="min-w-0 flex-1">
                  <h2 className="text-lg font-bold text-white truncate">{latestSnap.title}</h2>
                  <p className="text-sm text-[#00E5FF] font-semibold">
                    Chapter {latestSnap.chapterNumber} • Page {latestSnap.pageIndex + 1}/{latestSnap.totalPages}
                  </p>
                  <p className="text-xs text-zinc-400 mt-0.5 line-clamp-1">
                    Lumina Flow Ambient Immersion Active
                  </p>

                  <div className="w-full bg-[#27272A] h-2 rounded-full mt-3 overflow-hidden">
                    <div
                      className="h-full bg-gradient-to-r from-[#8B5CF6] to-[#00E5FF] rounded-full"
                      style={{ width: `${Math.round(latestSnap.progressPercent * 100)}%` }}
                    />
                  </div>
                </div>
              </div>

              <div className="flex gap-2 pt-1">
                <button
                  onClick={() => restoreSnap(latestSnap)}
                  className="flex-1 py-2.5 px-4 rounded-xl bg-gradient-to-r from-[#8B5CF6] to-[#7C3AED] hover:from-[#7C3AED] hover:to-[#6D28D9] text-white font-bold text-sm shadow-[0_0_15px_rgba(139,92,246,0.4)] flex items-center justify-center gap-2 transition-all"
                >
                  <Zap className="w-4 h-4 fill-white" />
                  Resume Session (1-Sec Snap)
                </button>
                <button
                  onClick={() => navigate('detail', latestSnap.mangaId)}
                  className="px-3.5 py-2.5 rounded-xl bg-[#18181E] border border-[#27272A] text-white hover:border-[#8B5CF6] text-xs font-semibold"
                >
                  Overview
                </button>
              </div>
            </div>
          </div>
        ) : (
          heroManga && (
            <div
              onClick={() => navigate('detail', heroManga.id)}
              className="relative rounded-2xl overflow-hidden border border-[#27272A] bg-[#09090B] p-5 cursor-pointer hover:border-[#8B5CF6] transition-all"
            >
              <div className="flex gap-4 items-center">
                <img
                  src={heroManga.thumbnailUrl}
                  alt={heroManga.title}
                  className="w-16 h-22 object-cover rounded-xl border border-[#27272A] shadow-md"
                />
                <div className="flex-1 min-w-0">
                  <span className="text-[10px] font-extrabold uppercase px-2 py-0.5 rounded bg-[#00E5FF]/20 text-[#00E5FF] border border-[#00E5FF]/30">
                    Featured Series
                  </span>
                  <h2 className="text-base font-bold text-white mt-1 truncate">{heroManga.title}</h2>
                  <p className="text-xs text-zinc-400 mt-0.5">{heroManga.author} • {heroManga.latestChapter}</p>
                </div>
              </div>
            </div>
          )
        )}

        {/* QUICK SHORTCUTS ROW */}
        <div className="grid grid-cols-4 gap-2">
          <button
            onClick={() => navigate('universe', 1)}
            className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#00E5FF]/50 rounded-xl p-2.5 flex flex-col items-center gap-1.5 transition-colors group"
          >
            <div className="w-8 h-8 rounded-lg bg-[#00E5FF]/10 flex items-center justify-center text-[#00E5FF] group-hover:scale-110 transition-transform">
              <Compass className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-semibold text-zinc-300">Universe Map</span>
          </button>

          <button
            onClick={() => navigate('ai')}
            className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-xl p-2.5 flex flex-col items-center gap-1.5 transition-colors group"
          >
            <div className="w-8 h-8 rounded-lg bg-[#8B5CF6]/10 flex items-center justify-center text-[#A78BFA] group-hover:scale-110 transition-transform">
              <Sparkles className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-semibold text-zinc-300">Lumina AI</span>
          </button>

          <button
            onClick={() => navigate('forge')}
            className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-amber-500/50 rounded-xl p-2.5 flex flex-col items-center gap-1.5 transition-colors group"
          >
            <div className="w-8 h-8 rounded-lg bg-amber-500/10 flex items-center justify-center text-amber-400 group-hover:scale-110 transition-transform">
              <Zap className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-semibold text-zinc-300">Forge Studio</span>
          </button>

          <button
            onClick={() => navigate('journey')}
            className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-emerald-500/50 rounded-xl p-2.5 flex flex-col items-center gap-1.5 transition-colors group"
          >
            <div className="w-8 h-8 rounded-lg bg-emerald-500/10 flex items-center justify-center text-emerald-400 group-hover:scale-110 transition-transform">
              <TrendingUp className="w-4 h-4" />
            </div>
            <span className="text-[10px] font-semibold text-zinc-300">Journey Map</span>
          </button>
        </div>

        {/* RECENT UPDATES CALLOUT */}
        {updates.length > 0 && (
          <div className="space-y-3">
            <div className="flex items-center justify-between">
              <h3 className="text-xs font-extrabold uppercase tracking-wider text-[#00E5FF] flex items-center gap-1.5">
                <span className="w-2 h-2 rounded-full bg-[#00E5FF] animate-ping inline-block" />
                Latest Releases
              </h3>
              <button
                onClick={() => navigate('updates')}
                className="text-xs text-zinc-400 hover:text-white flex items-center gap-0.5"
              >
                View all ({updates.length})
                <ChevronRight className="w-3.5 h-3.5" />
              </button>
            </div>

            <div className="bg-[#121216] border border-[#27272A] rounded-2xl divide-y divide-[#27272A]/60 overflow-hidden">
              {updates.slice(0, 2).map(u => (
                <div
                  key={u.id}
                  onClick={() => navigate('detail', u.mangaId)}
                  className="p-3 flex items-center justify-between gap-3 hover:bg-[#18181E] cursor-pointer transition-colors"
                >
                  <div className="flex items-center gap-3 min-w-0">
                    <img
                      src={u.coverUrl}
                      alt={u.title}
                      className="w-10 h-14 object-cover rounded-lg border border-[#27272A] flex-shrink-0"
                    />
                    <div className="min-w-0">
                      <h4 className="text-xs font-bold text-white truncate">{u.title}</h4>
                      <p className="text-[11px] text-[#A78BFA] font-medium">{u.chapterDisplay}</p>
                      <p className="text-[10px] text-zinc-400">{u.timeAgo}</p>
                    </div>
                  </div>
                  <div className="flex items-center gap-2">
                    {u.isDownloaded ? (
                      <span className="p-1.5 rounded-full bg-emerald-950/40 text-emerald-400 border border-emerald-500/30">
                        <CheckCircle2 className="w-3.5 h-3.5" />
                      </span>
                    ) : (
                      <span className="p-1.5 rounded-full bg-[#18181E] text-zinc-400 border border-[#27272A]">
                        <DownloadCloud className="w-3.5 h-3.5" />
                      </span>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}

        {/* TRENDING IN LUMINA DISCOVERY */}
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="text-xs font-extrabold uppercase tracking-wider text-zinc-400">
              Trending Manga Catalog
            </h3>
            <button
              onClick={() => navigate('library')}
              className="text-xs text-zinc-400 hover:text-white flex items-center gap-0.5"
            >
              Library <ChevronRight className="w-3.5 h-3.5" />
            </button>
          </div>

          <div className="grid grid-cols-2 sm:grid-cols-4 gap-3">
            {trendingMangas.map(m => (
              <div
                key={m.id}
                onClick={() => navigate('detail', m.id)}
                className="group relative bg-[#121216] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-2xl overflow-hidden cursor-pointer transition-all duration-200"
              >
                <div className="aspect-[3/4] relative overflow-hidden bg-zinc-900">
                  <img
                    src={m.thumbnailUrl}
                    alt={m.title}
                    className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-300"
                  />
                  <div className="absolute inset-0 bg-gradient-to-t from-black via-black/30 to-transparent" />
                  <span className="absolute top-2 right-2 px-1.5 py-0.5 rounded-md bg-black/70 backdrop-blur-md text-[10px] font-bold text-amber-400 border border-amber-500/30">
                    ★ {m.rating}
                  </span>
                </div>

                <div className="p-2.5">
                  <h4 className="text-xs font-bold text-white truncate group-hover:text-[#A78BFA] transition-colors">
                    {m.title}
                  </h4>
                  <p className="text-[10px] text-zinc-400 truncate mt-0.5">{m.latestChapter}</p>
                </div>
              </div>
            ))}
          </div>
        </div>
      </main>
    </div>
  );
};
