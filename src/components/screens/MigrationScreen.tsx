import React, { useState } from 'react';
import {
  ArrowLeft,
  ArrowRight,
  Shuffle,
  Search,
  CheckCircle2,
  AlertCircle,
  RefreshCw,
  Layers,
  Sparkles,
  BookOpen,
  Globe
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { SourceMigrationItem } from '../../types';

export const MigrationScreen: React.FC = () => {
  const {
    goBack,
    mangas,
    sources,
    migrationItems,
    executeMigration,
    startSmartMigration,
    showToast,
  } = useLumina();

  const [selectedMangaId, setSelectedMangaId] = useState<number | null>(mangas[0]?.id || null);
  const [selectedTargetSourceId, setSelectedTargetSourceId] = useState<string>(sources[1]?.id || 'asurascans');
  const [isProcessing, setIsProcessing] = useState(false);

  const selectedManga = mangas.find(m => m.id === selectedMangaId);

  const handleStartMigration = () => {
    if (!selectedMangaId || !selectedTargetSourceId) return;
    startSmartMigration(selectedMangaId, selectedTargetSourceId);
  };

  const handleExecute = async (id: string) => {
    setIsProcessing(true);
    await executeMigration(id);
    setIsProcessing(false);
  };

  return (
    <div className="min-h-screen bg-black text-white pb-24">
      {/* Header */}
      <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            id="back-btn"
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h2 className="text-sm font-black text-white flex items-center gap-2">
              <Shuffle className="w-4 h-4 text-[#00E5FF]" />
              Source Migration Hub
            </h2>
            <p className="text-[11px] text-zinc-400">Transfer reading progress & bookmarks across extensions</p>
          </div>
        </div>
      </header>

      <main className="max-w-2xl mx-auto px-4 pt-4 space-y-6">
        {/* Info Box */}
        <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 flex items-start gap-3">
          <Sparkles className="w-4 h-4 text-[#8B5CF6] flex-shrink-0 mt-0.5" />
          <div className="space-y-1 text-xs">
            <p className="font-bold text-white">Mihon / Tachiyomi Migration Engine</p>
            <p className="text-zinc-400 leading-relaxed">
              When a scanlation group disbands or changes domains, you can seamlessly migrate your manga entry, chapter read states, categories, and tracking bookmarks to another installed source.
            </p>
          </div>
        </div>

        {/* Create Migration Job */}
        <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-4">
          <h3 className="text-xs font-bold text-white flex items-center gap-2">
            <Shuffle className="w-4 h-4 text-[#8B5CF6]" />
            New Source Migration
          </h3>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-3">
            {/* Step 1: Select Library Manga */}
            <div className="space-y-1.5">
              <label className="text-[11px] font-semibold text-zinc-400">1. Select Manga in Library</label>
              <select
                value={selectedMangaId || ''}
                onChange={e => setSelectedMangaId(Number(e.target.value))}
                className="w-full bg-black border border-[#27272A] focus:border-[#8B5CF6] rounded-xl px-3 py-2.5 text-xs text-white outline-none"
              >
                {mangas.map(m => (
                  <option key={m.id} value={m.id}>
                    {m.title} ({m.source})
                  </option>
                ))}
              </select>
            </div>

            {/* Step 2: Target Source */}
            <div className="space-y-1.5">
              <label className="text-[11px] font-semibold text-zinc-400">2. Target Destination Source</label>
              <select
                value={selectedTargetSourceId}
                onChange={e => setSelectedTargetSourceId(e.target.value)}
                className="w-full bg-black border border-[#27272A] focus:border-[#8B5CF6] rounded-xl px-3 py-2.5 text-xs text-white outline-none"
              >
                {sources.map(s => (
                  <option key={s.id} value={s.id}>
                    {s.name} [{s.lang.toUpperCase()}]
                  </option>
                ))}
              </select>
            </div>
          </div>

          {selectedManga && (
            <div className="p-3 bg-black/60 rounded-xl border border-[#27272A] flex items-center justify-between">
              <div className="flex items-center gap-3">
                <img
                  src={selectedManga.thumbnailUrl}
                  alt={selectedManga.title}
                  className="w-10 h-14 object-cover rounded-lg border border-zinc-800"
                />
                <div>
                  <h4 className="text-xs font-bold text-white">{selectedManga.title}</h4>
                  <p className="text-[11px] text-zinc-400">
                    Current: <span className="text-[#00E5FF]">{selectedManga.source}</span> • {selectedManga.totalChapters} chapters
                  </p>
                </div>
              </div>

              <button
                id="match-source-btn"
                onClick={handleStartMigration}
                className="px-4 py-2 bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-bold rounded-xl shadow-md flex items-center gap-1.5 transition-colors"
              >
                <Search className="w-3.5 h-3.5" />
                Find Match
              </button>
            </div>
          )}
        </div>

        {/* Queued / Ready Migration Items */}
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="text-xs font-extrabold uppercase tracking-wider text-zinc-400 flex items-center gap-1.5">
              <Layers className="w-3.5 h-3.5 text-[#00E5FF]" />
              Migration Queue & History ({migrationItems.length})
            </h3>
          </div>

          <div className="space-y-3">
            {migrationItems.map(item => (
              <div
                key={item.id}
                id={`migration-job-${item.id}`}
                className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-3"
              >
                <div className="flex items-center justify-between gap-3">
                  <div className="flex items-center gap-3 min-w-0">
                    <img
                      src={item.mangaCover}
                      alt={item.mangaTitle}
                      className="w-11 h-16 object-cover rounded-xl border border-zinc-800 flex-shrink-0"
                    />
                    <div className="min-w-0 space-y-1">
                      <h4 className="text-sm font-bold text-white truncate">{item.mangaTitle}</h4>
                      <div className="flex items-center gap-2 text-[11px] text-zinc-400 flex-wrap">
                        <span className="px-2 py-0.5 bg-zinc-800 rounded-md text-zinc-300 font-medium">
                          {item.fromSourceName}
                        </span>
                        <ArrowRight className="w-3 h-3 text-[#8B5CF6]" />
                        <span className="px-2 py-0.5 bg-[#8B5CF6]/20 text-[#A78BFA] border border-[#8B5CF6]/40 rounded-md font-medium">
                          {item.toSourceName}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div className="flex flex-col items-end gap-1.5 flex-shrink-0">
                    <span className="px-2 py-0.5 bg-emerald-500/10 text-emerald-400 border border-emerald-500/30 rounded-full text-[10px] font-bold">
                      {item.matchScore}% Match
                    </span>
                    <span className="text-[10px] text-zinc-500">{item.targetChapterCount} Ch.</span>
                  </div>
                </div>

                <div className="pt-3 border-t border-[#27272A] flex items-center justify-between text-xs">
                  <span className="text-[11px] text-zinc-400 font-mono truncate max-w-[200px]">
                    Target: {item.targetMangaTitle}
                  </span>

                  {item.status === 'migrated' ? (
                    <span className="flex items-center gap-1 text-emerald-400 font-bold text-xs">
                      <CheckCircle2 className="w-4 h-4" /> Migrated
                    </span>
                  ) : (
                    <button
                      id={`execute-btn-${item.id}`}
                      onClick={() => handleExecute(item.id)}
                      disabled={isProcessing || item.status === 'searching'}
                      className="px-3.5 py-1.5 bg-[#00E5FF] hover:bg-[#00B4D8] text-black font-extrabold rounded-xl shadow-md text-xs flex items-center gap-1.5 transition-colors"
                    >
                      {item.status === 'searching' ? (
                        <RefreshCw className="w-3.5 h-3.5 animate-spin" />
                      ) : (
                        <Shuffle className="w-3.5 h-3.5" />
                      )}
                      Migrate Now
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        </div>
      </main>
    </div>
  );
};
