import React, { useState } from 'react';
import {
  History,
  Trash2,
  Play,
  Search,
  BookOpen,
  Calendar,
  Clock,
  Sparkles,
  ArrowRight,
  Filter,
  CheckCircle2,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { HistoryItem } from '../../types';

export const HistoryScreen: React.FC = () => {
  const {
    historyItems,
    removeHistoryItem,
    clearHistory,
    navigate,
    getManga,
  } = useLumina();

  const [searchQuery, setSearchQuery] = useState('');
  const [showClearConfirm, setShowClearConfirm] = useState(false);

  // Group history items by date relative to now
  const getGroupLabel = (timestamp: number) => {
    const now = new Date();
    const itemDate = new Date(timestamp);
    const diffDays = Math.floor((now.setHours(0, 0, 0, 0) - new Date(itemDate).setHours(0, 0, 0, 0)) / (1000 * 60 * 60 * 24));

    if (diffDays === 0) return 'Today';
    if (diffDays === 1) return 'Yesterday';
    if (diffDays === 2) return '2 days ago';
    if (diffDays === 3) return '3 days ago';
    
    // Format as DD/MM/YYYY
    const d = itemDate.getDate().toString().padStart(2, '0');
    const m = (itemDate.getMonth() + 1).toString().padStart(2, '0');
    const y = itemDate.getFullYear();
    return `${d}/${m}/${y}`;
  };

  const filteredItems = historyItems.filter(item => {
    if (!searchQuery.trim()) return true;
    const q = searchQuery.toLowerCase();
    return (
      item.mangaTitle.toLowerCase().includes(q) ||
      (item.chapterTitle && item.chapterTitle.toLowerCase().includes(q)) ||
      `chapter ${item.chapterNumber}`.includes(q)
    );
  });

  // Group by relative date
  const groupedItems: { [key: string]: HistoryItem[] } = {};
  filteredItems.forEach(item => {
    const time = item.readAt || item.timestamp;
    const group = getGroupLabel(time);
    if (!groupedItems[group]) {
      groupedItems[group] = [];
    }
    groupedItems[group].push(item);
  });

  const handleResume = (item: HistoryItem) => {
    navigate('reader', item.mangaId, item.chapterId, item.pageIndex);
  };

  const handleOpenDetail = (mangaId: number) => {
    navigate('detail', mangaId);
  };

  return (
    <div className="min-h-screen bg-[#0E0E12] pb-24 text-white">
      {/* Top Header */}
      <header className="sticky top-0 z-30 bg-[#121217]/95 backdrop-blur-md border-b border-[#27272A] px-4 py-3.5 flex items-center justify-between shadow-sm">
        <div className="flex items-center gap-3">
          <div className="w-9 h-9 rounded-xl bg-gradient-to-tr from-[#8B5CF6]/20 to-[#00E5FF]/20 border border-[#8B5CF6]/40 flex items-center justify-center text-[#A78BFA]">
            <History className="w-5 h-5" />
          </div>
          <div>
            <h1 className="text-lg font-bold tracking-tight text-zinc-100 flex items-center gap-2">
              History
              <span className="text-xs px-2 py-0.5 rounded-full bg-[#1F1F28] border border-[#2E2E3E] text-zinc-400 font-mono">
                {historyItems.length}
              </span>
            </h1>
            <p className="text-[11px] text-zinc-400">Past reading sessions & resume points</p>
          </div>
        </div>

        <div className="flex items-center gap-2">
          {historyItems.length > 0 && (
            <button
              onClick={() => setShowClearConfirm(true)}
              className="p-2 rounded-xl bg-[#1A1A22] hover:bg-rose-500/20 text-zinc-400 hover:text-rose-400 border border-[#2A2A38] hover:border-rose-500/40 transition-colors"
              title="Clear all reading history"
            >
              <Trash2 className="w-4 h-4" />
            </button>
          )}
        </div>
      </header>

      {/* Search Filter Bar */}
      <div className="px-4 pt-3 pb-2">
        <div className="relative">
          <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-zinc-400" />
          <input
            type="text"
            placeholder="Search reading history..."
            value={searchQuery}
            onChange={e => setSearchQuery(e.target.value)}
            className="w-full pl-10 pr-4 py-2 bg-[#17171F] border border-[#272733] focus:border-[#8B5CF6] focus:outline-none rounded-xl text-sm text-zinc-200 placeholder-zinc-500 transition-all"
          />
          {searchQuery && (
            <button
              onClick={() => setSearchQuery('')}
              className="absolute right-3 top-1/2 -translate-y-1/2 text-xs text-zinc-500 hover:text-zinc-300"
            >
              Clear
            </button>
          )}
        </div>
      </div>

      {/* History Items List */}
      <main className="px-4 pt-2 space-y-6">
        {Object.keys(groupedItems).length === 0 ? (
          <div className="py-20 flex flex-col items-center justify-center text-center px-4">
            <div className="w-16 h-16 rounded-2xl bg-[#1A1A24] border border-[#2C2C3C] flex items-center justify-center text-zinc-500 mb-4">
              <History className="w-8 h-8 stroke-1" />
            </div>
            <h3 className="text-base font-semibold text-zinc-200">No reading history found</h3>
            <p className="text-xs text-zinc-500 max-w-xs mt-1">
              {searchQuery
                ? `No history records matching "${searchQuery}"`
                : 'Chapters you read will automatically appear here for instant 1-tap resumption.'}
            </p>
            {!searchQuery && (
              <button
                onClick={() => navigate('library')}
                className="mt-5 px-5 py-2.5 rounded-xl bg-gradient-to-r from-[#8B5CF6] to-[#6366F1] text-white text-xs font-semibold shadow-[0_0_15px_rgba(139,92,246,0.3)] hover:opacity-95 transition-opacity"
              >
                Browse Library
              </button>
            )}
          </div>
        ) : (
          Object.entries(groupedItems).map(([groupTitle, items]) => (
            <div key={groupTitle} className="space-y-2.5">
              {/* Date Header matching Mihon */}
              <div className="flex items-center gap-2 pt-1 pb-1">
                <span className="text-xs font-semibold text-zinc-400 tracking-wider">
                  {groupTitle}
                </span>
                <div className="flex-1 h-px bg-[#22222D]" />
              </div>

              {/* Items in this date group */}
              <div className="space-y-2">
                {items.map(item => {
                  const manga = getManga(item.mangaId);
                  const cover = item.coverUrl || manga?.thumbnailUrl || 'https://cdn.myanimelist.net/images/manga/2/180031.jpg';
                  const timeVal = item.readAt || item.timestamp;
                  const timeFormatted = new Date(timeVal).toLocaleTimeString([], {
                    hour: 'numeric',
                    minute: '2-digit',
                  });
                  const progressPct =
                    item.progressPercent !== undefined
                      ? item.progressPercent
                      : item.totalPages > 0
                      ? (item.pageIndex + 1) / item.totalPages
                      : 0;

                  return (
                    <div
                      key={item.id}
                      className="group relative flex items-center gap-3.5 p-2.5 rounded-2xl bg-[#15151C] hover:bg-[#1A1A24] border border-[#242432] hover:border-[#3A3A4E] transition-all duration-200"
                    >
                      {/* Cover Thumbnail */}
                      <button
                        onClick={() => handleOpenDetail(item.mangaId)}
                        className="relative w-14 h-20 rounded-xl overflow-hidden flex-shrink-0 bg-zinc-900 border border-zinc-800 shadow-md group-hover:shadow-[0_0_12px_rgba(139,92,246,0.2)] transition-shadow"
                      >
                        <img
                          src={cover}
                          alt={item.mangaTitle}
                          className="w-full h-full object-cover"
                          referrerPolicy="no-referrer"
                          loading="lazy"
                        />
                        {progressPct > 0 && (
                          <div className="absolute bottom-0 left-0 right-0 h-1 bg-black/60">
                            <div
                              className="h-full bg-[#00E5FF]"
                              style={{ width: `${Math.min(100, Math.round(progressPct * 100))}%` }}
                            />
                          </div>
                        )}
                      </button>

                      {/* Content Info */}
                      <div className="flex-1 min-w-0 pr-1">
                        <button
                          onClick={() => handleOpenDetail(item.mangaId)}
                          className="text-left w-full truncate font-semibold text-sm text-zinc-100 hover:text-[#A78BFA] transition-colors"
                        >
                          {item.mangaTitle}
                        </button>

                        <div className="flex items-center gap-2 mt-1">
                          <span className="text-xs text-zinc-400 font-medium">
                            {item.chapterTitle || `Ch. ${item.chapterNumber}`}
                          </span>
                          <span className="text-[10px] text-zinc-400 font-mono">
                            • {timeFormatted}
                          </span>
                        </div>

                        {/* Progress status */}
                        <div className="flex items-center gap-2 mt-2">
                          <span className="text-[10px] px-2 py-0.5 rounded-md bg-[#1F1F2C] border border-[#2B2B3E] text-zinc-400 font-mono">
                            Pg {item.pageIndex + 1}/{item.totalPages}
                          </span>
                          {progressPct >= 0.95 && (
                            <span className="text-[10px] text-emerald-400 flex items-center gap-1">
                              <CheckCircle2 className="w-3 h-3" /> Finished
                            </span>
                          )}
                        </div>
                      </div>

                      {/* Action buttons */}
                      <div className="flex items-center gap-1.5 flex-shrink-0">
                        <button
                          onClick={() => handleResume(item)}
                          className="w-9 h-9 rounded-xl bg-[#8B5CF6]/20 hover:bg-[#8B5CF6] text-[#A78BFA] hover:text-white border border-[#8B5CF6]/40 flex items-center justify-center transition-all shadow-sm active:scale-95"
                          title="Resume Chapter"
                        >
                          <Play className="w-4 h-4 fill-current ml-0.5" />
                        </button>
                        <button
                          onClick={() => removeHistoryItem(item.id)}
                          className="w-8 h-8 rounded-xl bg-transparent hover:bg-rose-500/10 text-zinc-500 hover:text-rose-400 flex items-center justify-center transition-colors"
                          title="Remove from history"
                        >
                          <Trash2 className="w-3.5 h-3.5" />
                        </button>
                      </div>
                    </div>
                  );
                })}
              </div>
            </div>
          ))
        )}
      </main>

      {/* Clear Confirmation Modal */}
      {showClearConfirm && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div className="w-full max-w-xs rounded-2xl bg-[#161620] border border-[#2C2C3E] p-5 shadow-2xl space-y-4 animate-scale-in">
            <div className="w-10 h-10 rounded-xl bg-rose-500/20 text-rose-400 flex items-center justify-center border border-rose-500/30">
              <Trash2 className="w-5 h-5" />
            </div>
            <div>
              <h3 className="text-base font-bold text-zinc-100">Clear reading history?</h3>
              <p className="text-xs text-zinc-400 mt-1">
                This will remove all reading history records. Your library manga and saved downloads will remain unaffected.
              </p>
            </div>
            <div className="flex gap-2 pt-2">
              <button
                onClick={() => setShowClearConfirm(false)}
                className="flex-1 py-2 rounded-xl bg-[#22222E] hover:bg-[#2A2A38] text-zinc-300 text-xs font-semibold transition-colors"
              >
                Cancel
              </button>
              <button
                onClick={() => {
                  clearHistory();
                  setShowClearConfirm(false);
                }}
                className="flex-1 py-2 rounded-xl bg-rose-600 hover:bg-rose-500 text-white text-xs font-semibold shadow-[0_0_12px_rgba(225,29,72,0.4)] transition-colors"
              >
                Clear All
              </button>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};
