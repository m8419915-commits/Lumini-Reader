import React, { useState } from 'react';
import {
  ArrowLeft,
  CloudSync,
  Plus,
  Trash2,
  ExternalLink,
  CheckCircle2,
  RefreshCw,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';

export const RepositoriesScreen: React.FC = () => {
  const { goBack, showToast, fetchExtensionsRepo, isFetchingRepo } = useLumina();
  const [repoUrls, setRepoUrls] = useState<string[]>([
    'https://raw.githubusercontent.com/keiyoushi/extensions/repo/index.min.json',
  ]);
  const [newUrl, setNewUrl] = useState('');

  const handleAddRepo = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newUrl.trim()) return;
    if (repoUrls.includes(newUrl.trim())) {
      showToast('Repository already added');
      return;
    }
    setRepoUrls([...repoUrls, newUrl.trim()]);
    setNewUrl('');
    showToast('Repository added to source index');
  };

  const handleRemoveRepo = (url: string) => {
    setRepoUrls(repoUrls.filter(r => r !== url));
    showToast('Repository removed');
  };

  return (
    <div className="min-h-screen bg-black text-white pb-16">
      <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h2 className="text-sm font-black text-white">Repository Manager</h2>
            <p className="text-[11px] text-zinc-400">Keiyoushi & third-party index feeds</p>
          </div>
        </div>
      </header>

      <main className="max-w-xl mx-auto px-4 pt-4 space-y-5">
        {/* Add Repository Form */}
        <form onSubmit={handleAddRepo} className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-3">
          <h3 className="text-xs font-bold text-white flex items-center gap-2">
            <Plus className="w-4 h-4 text-[#8B5CF6]" />
            Add Extension Repository
          </h3>

          <div className="flex gap-2">
            <input
              type="url"
              required
              value={newUrl}
              onChange={e => setNewUrl(e.target.value)}
              placeholder="https://.../index.min.json"
              className="flex-1 bg-black border border-[#27272A] focus:border-[#8B5CF6] rounded-xl px-3 py-2 text-xs text-white placeholder-zinc-500 outline-none"
            />
            <button
              type="submit"
              className="px-4 py-2 bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-bold rounded-xl shadow-md"
            >
              Add
            </button>
          </div>
        </form>

        {/* Existing Repositories */}
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="text-xs font-extrabold uppercase tracking-wider text-zinc-400">
              Active Repositories ({repoUrls.length})
            </h3>

            <button
              onClick={fetchExtensionsRepo}
              disabled={isFetchingRepo}
              className="text-xs text-[#00E5FF] hover:underline flex items-center gap-1 font-semibold"
            >
              <RefreshCw className={`w-3.5 h-3.5 ${isFetchingRepo ? 'animate-spin' : ''}`} />
              Refresh All
            </button>
          </div>

          <div className="space-y-2.5">
            {repoUrls.map((url, idx) => (
              <div
                key={idx}
                className="bg-[#121216] border border-[#27272A] rounded-2xl p-3.5 flex items-center justify-between gap-3"
              >
                <div className="min-w-0 flex-1 space-y-0.5">
                  <div className="flex items-center gap-2">
                    <CheckCircle2 className="w-4 h-4 text-emerald-400 flex-shrink-0" />
                    <h4 className="text-xs font-bold text-white truncate">
                      {url.includes('keiyoushi') ? 'Official Keiyoushi Extensions' : `Custom Source ${idx + 1}`}
                    </h4>
                  </div>
                  <p className="text-[11px] text-zinc-400 truncate font-mono">{url}</p>
                </div>

                {repoUrls.length > 1 && (
                  <button
                    onClick={() => handleRemoveRepo(url)}
                    className="p-2 rounded-xl text-rose-400 hover:bg-rose-950/40"
                    title="Remove Repository"
                  >
                    <Trash2 className="w-4 h-4" />
                  </button>
                )}
              </div>
            ))}
          </div>
        </div>
      </main>
    </div>
  );
};
