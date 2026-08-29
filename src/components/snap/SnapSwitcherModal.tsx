import React from 'react';
import { Zap, X, Trash2, BookOpen, Clock } from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';

export const SnapSwitcherModal: React.FC = () => {
  const { isSnapSwitcherOpen, setSnapSwitcherOpen, snaps, restoreSnap, deleteSnap } = useLumina();

  if (!isSnapSwitcherOpen) return null;

  return (
    <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-md flex items-end sm:items-center justify-center p-0 sm:p-4">
      <div
        className="w-full max-w-xl bg-[#09090B] border border-[#27272A] rounded-t-2xl sm:rounded-2xl p-5 shadow-2xl max-h-[85vh] flex flex-col animate-in fade-in slide-in-from-bottom-6 duration-200"
      >
        {/* Header */}
        <div className="flex items-center justify-between pb-4 border-b border-[#27272A]">
          <div className="flex items-center gap-2.5">
            <div className="w-8 h-8 rounded-xl bg-[#00E5FF]/10 border border-[#00E5FF]/30 flex items-center justify-center text-[#00E5FF]">
              <Zap className="w-4 h-4 fill-[#00E5FF]" />
            </div>
            <div>
              <h2 className="text-base font-bold text-white flex items-center gap-2">
                Lumina Snap Switcher
                <span className="text-[10px] bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 text-[#A78BFA] px-1.5 py-0.5 rounded font-mono">
                  1-Sec Restore
                </span>
              </h2>
              <p className="text-xs text-zinc-400">
                Instant zero-friction resume for up to 10 active reading sessions.
              </p>
            </div>
          </div>
          <button
            onClick={() => setSnapSwitcherOpen(false)}
            className="w-8 h-8 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-zinc-400 hover:text-white"
          >
            <X className="w-4 h-4" />
          </button>
        </div>

        {/* Snap Cards List */}
        <div className="overflow-y-auto py-4 space-y-3 flex-1">
          {snaps.length === 0 ? (
            <div className="py-12 text-center text-zinc-500">
              <Zap className="w-8 h-8 mx-auto mb-2 text-zinc-600 opacity-50" />
              <p className="text-sm font-medium">No saved reading snapshots yet</p>
              <p className="text-xs text-zinc-600 mt-1">Open any manga to automatically record reading snaps.</p>
            </div>
          ) : (
            snaps.map(snap => {
              const minutesAgo = Math.round((Date.now() - snap.timestamp) / 60000);
              const timeDisplay = minutesAgo < 1 ? 'Just now' : `${minutesAgo}m ago`;

              return (
                <div
                  key={snap.mangaId}
                  className="group relative bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#00E5FF]/50 rounded-xl p-3 flex items-center justify-between gap-3 transition-all cursor-pointer"
                  onClick={() => restoreSnap(snap)}
                >
                  <div className="flex items-center gap-3.5 flex-1 min-w-0">
                    <img
                      src={snap.coverUrl}
                      alt={snap.title}
                      className="w-12 h-16 object-cover rounded-lg border border-[#27272A] shadow-md flex-shrink-0"
                    />
                    <div className="min-w-0 flex-1">
                      <h4 className="text-sm font-bold text-white truncate group-hover:text-[#00E5FF] transition-colors">
                        {snap.title}
                      </h4>
                      <div className="flex items-center gap-2 mt-1">
                        <span className="text-xs font-semibold text-[#A78BFA] flex items-center gap-1">
                          <BookOpen className="w-3 h-3" />
                          Ch. {snap.chapterNumber} • Page {snap.pageIndex + 1}/{snap.totalPages}
                        </span>
                        <span className="text-[10px] text-zinc-400 flex items-center gap-0.5">
                          <Clock className="w-2.5 h-2.5" />
                          {timeDisplay}
                        </span>
                      </div>

                      {/* Progress line */}
                      <div className="w-full bg-[#27272A] h-1.5 rounded-full mt-2 overflow-hidden">
                        <div
                          className="bg-gradient-to-r from-[#8B5CF6] to-[#00E5FF] h-full rounded-full"
                          style={{ width: `${Math.round(snap.progressPercent * 100)}%` }}
                        />
                      </div>
                    </div>
                  </div>

                  <div className="flex items-center gap-2">
                    <button
                      onClick={(e) => {
                        e.stopPropagation();
                        deleteSnap(snap.mangaId);
                      }}
                      className="p-2 rounded-lg text-zinc-500 hover:text-rose-400 hover:bg-rose-950/30 transition-colors"
                      title="Remove Snap"
                    >
                      <Trash2 className="w-4 h-4" />
                    </button>
                    <button
                      onClick={() => restoreSnap(snap)}
                      className="px-3 py-1.5 rounded-lg bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-bold shadow-[0_0_10px_rgba(139,92,246,0.3)]"
                    >
                      Resume
                    </button>
                  </div>
                </div>
              );
            })
          )}
        </div>

        {/* Footer info */}
        <div className="pt-3 border-t border-[#27272A] flex items-center justify-between text-xs text-zinc-400">
          <span>Active Snaps: {snaps.length}/10</span>
          <button
            onClick={() => setSnapSwitcherOpen(false)}
            className="text-zinc-300 hover:text-white font-medium"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};
