import React from 'react';
import { DownloadCloud, CheckCircle2, Download, Bell } from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { HeaderBar } from '../navigation/HeaderBar';

export const UpdatesScreen: React.FC = () => {
  const { updates, downloadUpdate, downloadAllUpdates, navigate } = useLumina();
  const unreadCount = updates.filter(u => !u.isDownloaded).length;

  return (
    <div className="min-h-screen bg-black text-white pb-24">
      <HeaderBar />

      <main className="max-w-xl mx-auto px-4 pt-3 space-y-4">
        {/* Banner Pill */}
        <div className="p-3 rounded-2xl bg-[#8B5CF6]/15 border border-[#8B5CF6]/40 flex items-center justify-between">
          <div className="flex items-center gap-2.5">
            <Bell className="w-4 h-4 text-[#A78BFA]" />
            <span className="text-xs font-bold text-white">
              {unreadCount} new chapters ready to download
            </span>
          </div>
          {unreadCount > 0 && (
            <button
              onClick={downloadAllUpdates}
              className="px-3 py-1 rounded-xl bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-bold shadow-md flex items-center gap-1.5"
            >
              <DownloadCloud className="w-3.5 h-3.5" />
              Download All
            </button>
          )}
        </div>

        {/* Updates List */}
        <div className="space-y-3">
          <h2 className="text-lg font-bold text-white">Chapter Updates</h2>

          {updates.map(item => (
            <div
              key={item.id}
              onClick={() => navigate('detail', item.mangaId)}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/40 rounded-2xl p-3.5 flex items-center justify-between gap-3 cursor-pointer transition-all"
            >
              <div className="flex items-center gap-3.5 min-w-0 flex-1">
                <img
                  src={item.coverUrl}
                  alt={item.title}
                  className="w-12 h-16 object-cover rounded-xl border border-[#27272A] shadow-md flex-shrink-0"
                />
                <div className="min-w-0">
                  <h4 className="text-sm font-bold text-white truncate">{item.title}</h4>
                  <p className="text-xs font-semibold text-[#A78BFA] mt-0.5">{item.chapterDisplay}</p>
                  <p className="text-[11px] text-zinc-400 mt-1">{item.timeAgo}</p>
                </div>
              </div>

              <button
                onClick={(e) => {
                  e.stopPropagation();
                  downloadUpdate(item.id);
                }}
                className={`w-10 h-10 rounded-full flex items-center justify-center transition-all ${
                  item.isDownloaded
                    ? 'bg-emerald-950/40 text-emerald-400 border border-emerald-500/30'
                    : 'bg-[#18181E] border border-[#27272A] text-white hover:border-[#8B5CF6]'
                }`}
                title={item.isDownloaded ? 'Downloaded' : 'Download for offline'}
              >
                {item.isDownloaded ? (
                  <CheckCircle2 className="w-5 h-5 text-emerald-400" />
                ) : (
                  <Download className="w-4 h-4 text-zinc-300" />
                )}
              </button>
            </div>
          ))}
        </div>
      </main>
    </div>
  );
};
