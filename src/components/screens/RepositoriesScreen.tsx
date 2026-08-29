import React, { useState } from 'react';
import {
  ArrowLeft,
  Plus,
  Trash2,
  ExternalLink,
  CheckCircle2,
  RefreshCw,
  ShieldCheck,
  Globe,
  Copy,
  Info,
  Layers,
  Flame,
  AlertTriangle,
  ToggleLeft,
  ToggleRight,
  Database
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { ExtensionStore } from '../../types';

export const RepositoriesScreen: React.FC = () => {
  const {
    goBack,
    showToast,
    extensionStores,
    addExtensionStore,
    removeExtensionStore,
    toggleExtensionStore,
    fetchExtensionsRepo,
    isFetchingRepo,
  } = useLumina();

  const [name, setName] = useState('');
  const [baseUrl, setBaseUrl] = useState('');
  const [selectedPreset, setSelectedPreset] = useState<string | null>(null);

  const presets = [
    {
      name: 'Keiyoushi Official',
      url: 'https://raw.githubusercontent.com/keiyoushi/extensions/repo',
      index: 'https://raw.githubusercontent.com/keiyoushi/extensions/repo/index.min.json',
      web: 'https://keiyoushi.github.io/extensions/',
      desc: '1,200+ curated extensions with automatic updates',
      isOfficial: true,
    },
    {
      name: 'Komikku Community',
      url: 'https://raw.githubusercontent.com/komikku-app/komikku-extensions/repo',
      index: 'https://raw.githubusercontent.com/komikku-app/komikku-extensions/repo/index.min.json',
      web: 'https://komikku.app',
      desc: 'Community-maintained scanlator & indie raw extensions',
      isOfficial: false,
    },
    {
      name: 'MangaDex Direct Feed',
      url: 'https://api.mangadex.org',
      index: 'https://raw.githubusercontent.com/mangadex-pub/repo/master/index.min.json',
      web: 'https://mangadex.org',
      desc: 'Direct repository for fast MangaDex scanlation groups',
      isOfficial: false,
    },
  ];

  const handleApplyPreset = (preset: typeof presets[0]) => {
    setName(preset.name);
    setBaseUrl(preset.url);
    setSelectedPreset(preset.name);
  };

  const handleAddRepo = (e: React.FormEvent) => {
    e.preventDefault();
    if (!name.trim() || !baseUrl.trim()) return;

    // Check if duplicate
    const exists = extensionStores.some(
      s => s.baseUrl.toLowerCase() === baseUrl.trim().toLowerCase()
    );
    if (exists) {
      showToast('This repository endpoint is already added');
      return;
    }

    addExtensionStore({
      name: name.trim(),
      baseUrl: baseUrl.trim(),
      indexUrl: `${baseUrl.trim().replace(/\/$/, '')}/index.min.json`,
      website: baseUrl.trim(),
      isOfficial: baseUrl.includes('keiyoushi'),
      isPinned: false,
      enabled: true,
      fingerprint: `SHA256:${Math.random().toString(36).substring(2, 15)}...`,
    });

    setName('');
    setBaseUrl('');
    setSelectedPreset(null);
  };

  const handleCopyUrl = (url: string) => {
    navigator.clipboard?.writeText(url);
    showToast('Repository URL copied to clipboard');
  };

  return (
    <div className="min-h-screen bg-black text-white pb-20">
      {/* Header */}
      <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            id="back-to-more-btn"
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-[#8B5CF6] transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h2 className="text-sm font-black text-white flex items-center gap-2">
              <Database className="w-4 h-4 text-[#8B5CF6]" />
              Repository Manager
            </h2>
            <p className="text-[11px] text-zinc-400">Mihon & Tachiyomi Extension Repositories</p>
          </div>
        </div>

        <button
          id="sync-all-repos-btn"
          onClick={fetchExtensionsRepo}
          disabled={isFetchingRepo}
          className="px-3 py-1.5 bg-[#8B5CF6]/20 hover:bg-[#8B5CF6]/30 border border-[#8B5CF6]/40 text-[#8B5CF6] rounded-xl text-xs font-bold flex items-center gap-1.5 transition-colors"
        >
          <RefreshCw className={`w-3.5 h-3.5 ${isFetchingRepo ? 'animate-spin' : ''}`} />
          {isFetchingRepo ? 'Syncing...' : 'Sync All'}
        </button>
      </header>

      <main className="max-w-2xl mx-auto px-4 pt-4 space-y-6">
        {/* Quick Info Banner */}
        <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 flex items-start gap-3">
          <Info className="w-4 h-4 text-[#00E5FF] flex-shrink-0 mt-0.5" />
          <div className="space-y-1 text-xs">
            <p className="font-bold text-white">Mihon / Tachiyomi Extension Protocol</p>
            <p className="text-zinc-400 leading-relaxed">
              Lumina Reader fetches extension metadata directly from repository index endpoints (<code className="text-[#00E5FF] font-mono">index.min.json</code>). You can add third-party repositories or official community sources.
            </p>
          </div>
        </div>

        {/* Add Repository Card */}
        <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-4">
          <div className="flex items-center justify-between">
            <h3 className="text-xs font-bold text-white flex items-center gap-2">
              <Plus className="w-4 h-4 text-[#8B5CF6]" />
              Add Repository
            </h3>
            <span className="text-[10px] text-zinc-500 font-mono">HTTPS JSON Feeds</span>
          </div>

          {/* Quick Presets */}
          <div className="space-y-1.5">
            <p className="text-[10px] font-semibold text-zinc-400 uppercase tracking-wider">Quick Presets</p>
            <div className="flex flex-wrap gap-2">
              {presets.map(p => (
                <button
                  key={p.name}
                  type="button"
                  onClick={() => handleApplyPreset(p)}
                  className={`px-2.5 py-1.5 rounded-lg text-xs font-medium border transition-colors flex items-center gap-1.5 ${
                    selectedPreset === p.name
                      ? 'bg-[#8B5CF6]/30 border-[#8B5CF6] text-white'
                      : 'bg-black/60 border-[#27272A] text-zinc-300 hover:border-zinc-500'
                  }`}
                >
                  {p.isOfficial ? <ShieldCheck className="w-3 h-3 text-[#00E5FF]" /> : <Flame className="w-3 h-3 text-amber-400" />}
                  {p.name}
                </button>
              ))}
            </div>
          </div>

          {/* Form */}
          <form onSubmit={handleAddRepo} className="space-y-3 pt-1">
            <div className="space-y-1">
              <label className="text-[11px] font-medium text-zinc-400">Repository Name</label>
              <input
                id="repo-name-input"
                type="text"
                required
                value={name}
                onChange={e => setName(e.target.value)}
                placeholder="e.g. Keiyoushi Community"
                className="w-full bg-black border border-[#27272A] focus:border-[#8B5CF6] rounded-xl px-3 py-2 text-xs text-white placeholder-zinc-500 outline-none"
              />
            </div>

            <div className="space-y-1">
              <label className="text-[11px] font-medium text-zinc-400">Base Repository URL or Index URL</label>
              <div className="flex gap-2">
                <input
                  id="repo-url-input"
                  type="url"
                  required
                  value={baseUrl}
                  onChange={e => setBaseUrl(e.target.value)}
                  placeholder="https://raw.githubusercontent.com/.../repo"
                  className="flex-1 bg-black border border-[#27272A] focus:border-[#8B5CF6] rounded-xl px-3 py-2 text-xs text-white placeholder-zinc-500 outline-none font-mono"
                />
                <button
                  id="submit-repo-btn"
                  type="submit"
                  className="px-4 py-2 bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-bold rounded-xl shadow-md transition-colors"
                >
                  Add
                </button>
              </div>
            </div>
          </form>
        </div>

        {/* Active Repositories List */}
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="text-xs font-extrabold uppercase tracking-wider text-zinc-400 flex items-center gap-1.5">
              <Layers className="w-3.5 h-3.5 text-[#8B5CF6]" />
              Active Repositories ({extensionStores.length})
            </h3>
            <span className="text-[11px] text-zinc-500">
              {extensionStores.reduce((acc, s) => acc + (s.enabled ? s.totalExtensions : 0), 0)} packages indexed
            </span>
          </div>

          <div className="space-y-3">
            {extensionStores.map(store => (
              <div
                key={store.id}
                id={`repo-card-${store.id}`}
                className={`bg-[#121216] border rounded-2xl p-4 space-y-3 transition-colors ${
                  store.enabled ? 'border-[#27272A]' : 'border-[#27272A]/50 opacity-60'
                }`}
              >
                <div className="flex items-start justify-between gap-3">
                  <div className="space-y-1 min-w-0">
                    <div className="flex items-center gap-2 flex-wrap">
                      <h4 className="text-sm font-bold text-white truncate">{store.name}</h4>
                      {store.isOfficial && (
                        <span className="px-2 py-0.5 bg-[#00E5FF]/10 text-[#00E5FF] border border-[#00E5FF]/30 rounded-full text-[10px] font-bold flex items-center gap-1">
                          <ShieldCheck className="w-3 h-3" /> Official
                        </span>
                      )}
                      <span className="px-2 py-0.5 bg-zinc-800 text-zinc-300 rounded-full text-[10px] font-mono">
                        {store.totalExtensions} extensions
                      </span>
                    </div>
                    <p className="text-[11px] text-zinc-400 font-mono truncate">{store.baseUrl}</p>
                  </div>

                  {/* Toggle enable / disable */}
                  <button
                    onClick={() => toggleExtensionStore(store.id)}
                    className="p-1 text-zinc-400 hover:text-white transition-colors"
                    title={store.enabled ? 'Disable repository' : 'Enable repository'}
                  >
                    {store.enabled ? (
                      <ToggleRight className="w-7 h-7 text-[#8B5CF6]" />
                    ) : (
                      <ToggleLeft className="w-7 h-7 text-zinc-600" />
                    )}
                  </button>
                </div>

                {/* Metadata & Actions */}
                <div className="pt-2 border-t border-[#27272A] flex items-center justify-between text-xs text-zinc-400">
                  <div className="flex items-center gap-3 text-[11px]">
                    <span className="flex items-center gap-1">
                      <CheckCircle2 className="w-3 h-3 text-emerald-400" />
                      Synced {store.lastSynced}
                    </span>
                  </div>

                  <div className="flex items-center gap-2">
                    <button
                      onClick={() => handleCopyUrl(store.baseUrl)}
                      className="p-1.5 bg-black border border-[#27272A] hover:border-zinc-500 text-zinc-300 rounded-lg text-xs"
                      title="Copy URL"
                    >
                      <Copy className="w-3.5 h-3.5" />
                    </button>

                    {store.website && (
                      <a
                        href={store.website}
                        target="_blank"
                        rel="noreferrer"
                        className="p-1.5 bg-black border border-[#27272A] hover:border-zinc-500 text-zinc-300 rounded-lg text-xs flex items-center"
                        title="Open Website"
                      >
                        <ExternalLink className="w-3.5 h-3.5" />
                      </a>
                    )}

                    {extensionStores.length > 1 && (
                      <button
                        onClick={() => removeExtensionStore(store.id)}
                        className="p-1.5 bg-rose-950/30 border border-rose-900/40 hover:bg-rose-900/50 text-rose-400 rounded-lg text-xs"
                        title="Remove Repository"
                      >
                        <Trash2 className="w-3.5 h-3.5" />
                      </button>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </main>
    </div>
  );
};

