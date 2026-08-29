import React, { useState } from 'react';
import {
  Globe,
  Puzzle,
  FolderArchive,
  CloudSync,
  ChevronRight,
  Download,
  Trash2,
  RefreshCw,
  Search,
  CheckCircle,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { HeaderBar } from '../navigation/HeaderBar';

export const ExploreScreen: React.FC = () => {
  const {
    extensions,
    toggleExtensionInstall,
    fetchExtensionsRepo,
    isFetchingRepo,
    navigate,
  } = useLumina();

  const [activeTab, setActiveTab] = useState<'sources' | 'extensions'>('sources');
  const [selectedLang, setSelectedLang] = useState<string>('All');
  const [search, setSearch] = useState('');

  const languages = ['All', 'en', 'ja', 'ko', 'zh', 'es', 'fr'];

  const filteredExtensions = extensions.filter(ext => {
    const matchesLang =
      selectedLang === 'All' ||
      ext.lang.toLowerCase() === selectedLang.toLowerCase() ||
      ext.lang === 'all';
    const matchesSearch =
      search.trim() === '' ||
      ext.name.toLowerCase().includes(search.toLowerCase()) ||
      ext.packageName.toLowerCase().includes(search.toLowerCase());
    return matchesLang && matchesSearch;
  });

  return (
    <div className="min-h-screen bg-black text-white pb-24">
      <HeaderBar
        showRepoManager={true}
        onOpenRepoManager={() => navigate('repositories')}
      />

      <main className="max-w-xl mx-auto px-4 pt-3 space-y-4">
        {/* Tab Switcher (Sources vs Extensions) */}
        <div className="p-1 rounded-2xl bg-[#121216] border border-[#27272A] flex">
          <button
            onClick={() => setActiveTab('sources')}
            className={`flex-1 py-2 rounded-xl text-xs font-bold transition-all ${
              activeTab === 'sources'
                ? 'bg-[#8B5CF6] text-white shadow-[0_0_12px_rgba(139,92,246,0.4)]'
                : 'text-zinc-400 hover:text-white'
            }`}
          >
            Sources
          </button>
          <button
            onClick={() => setActiveTab('extensions')}
            className={`flex-1 py-2 rounded-xl text-xs font-bold transition-all ${
              activeTab === 'extensions'
                ? 'bg-[#8B5CF6] text-white shadow-[0_0_12px_rgba(139,92,246,0.4)]'
                : 'text-zinc-400 hover:text-white'
            }`}
          >
            Extensions ({extensions.filter(e => e.installed).length})
          </button>
        </div>

        {activeTab === 'sources' ? (
          <div className="space-y-4">
            <div className="flex items-center justify-between">
              <span className="text-[11px] font-extrabold uppercase tracking-wider text-[#00E5FF]">
                Installed Sources
              </span>
              <span className="text-xs text-zinc-400">3 Online</span>
            </div>

            {/* Source 1: MangaDex */}
            <div
              onClick={() => navigate('detail', 1)}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-all"
            >
              <div className="flex items-center gap-3.5">
                <div className="w-11 h-11 rounded-full bg-[#8B5CF6]/15 border border-[#8B5CF6]/40 flex items-center justify-center text-[#A78BFA]">
                  <Globe className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="text-sm font-bold text-white">MangaDex</h3>
                  <p className="text-xs text-zinc-400">EN • Multi • v1.4.2 [Latest]</p>
                </div>
              </div>
              <span className="px-2 py-0.5 rounded-md bg-[#00E5FF]/10 text-[#00E5FF] border border-[#00E5FF]/20 text-[10px] font-bold">
                ONLINE
              </span>
            </div>

            {/* Source 2: Keiyoushi */}
            <div
              onClick={() => setActiveTab('extensions')}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#00E5FF]/50 rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-all"
            >
              <div className="flex items-center gap-3.5">
                <div className="w-11 h-11 rounded-full bg-[#00E5FF]/15 border border-[#00E5FF]/40 flex items-center justify-center text-[#00E5FF]">
                  <Puzzle className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="text-sm font-bold text-white">Keiyoushi Repository</h3>
                  <p className="text-xs text-zinc-400">EN • Repository Index • v2.0.1</p>
                </div>
              </div>
              <span className="px-2 py-0.5 rounded-md bg-emerald-500/10 text-emerald-400 border border-emerald-500/20 text-[10px] font-bold">
                ACTIVE
              </span>
            </div>

            {/* Source 3: Local Storage */}
            <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 flex items-center justify-between">
              <div className="flex items-center gap-3.5">
                <div className="w-11 h-11 rounded-full bg-amber-500/15 border border-amber-500/40 flex items-center justify-center text-amber-400">
                  <FolderArchive className="w-5 h-5" />
                </div>
                <div>
                  <h3 className="text-sm font-bold text-white">Local Storage</h3>
                  <p className="text-xs text-zinc-400">System Folder (CBZ, CBR, EPUB)</p>
                </div>
              </div>
              <span className="px-2 py-0.5 rounded-md bg-zinc-800 text-zinc-300 border border-zinc-700 text-[10px] font-bold">
                READY
              </span>
            </div>

            {/* Repository Manager Shortcut */}
            <div
              onClick={() => navigate('repositories')}
              className="bg-[#121216]/60 hover:bg-[#18181E] border border-[#27272A] rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3.5">
                <CloudSync className="w-5 h-5 text-[#A78BFA]" />
                <div>
                  <h4 className="text-sm font-bold text-white">Repository Manager</h4>
                  <p className="text-xs text-zinc-400">Manage ProtoBuf & Keiyoushi endpoints</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>
          </div>
        ) : (
          <div className="space-y-4">
            {/* Search & Language Filters */}
            <div className="space-y-2.5">
              <div className="relative">
                <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-zinc-500" />
                <input
                  type="text"
                  value={search}
                  onChange={e => setSearch(e.target.value)}
                  placeholder="Search extension packages..."
                  className="w-full bg-[#121216] border border-[#27272A] focus:border-[#8B5CF6] rounded-xl pl-10 pr-4 py-2 text-sm text-white placeholder-zinc-500 outline-none"
                />
              </div>

              <div className="flex items-center justify-between gap-2">
                <div className="flex items-center gap-1.5 overflow-x-auto no-scrollbar">
                  {languages.map(lang => {
                    const isSelected = selectedLang === lang;
                    return (
                      <button
                        key={lang}
                        onClick={() => setSelectedLang(lang)}
                        className={`px-3 py-1 rounded-full text-xs font-bold uppercase transition-all ${
                          isSelected
                            ? 'bg-[#8B5CF6]/30 border border-[#A78BFA] text-white'
                            : 'bg-[#121216] border border-[#27272A] text-zinc-400 hover:text-zinc-200'
                        }`}
                      >
                        {lang}
                      </button>
                    );
                  })}
                </div>

                <button
                  onClick={fetchExtensionsRepo}
                  disabled={isFetchingRepo}
                  className="p-1.5 rounded-xl bg-[#121216] border border-[#27272A] hover:border-[#8B5CF6] text-zinc-300 hover:text-white flex-shrink-0"
                  title="Sync Keiyoushi Repository"
                >
                  <RefreshCw className={`w-4 h-4 ${isFetchingRepo ? 'animate-spin text-[#8B5CF6]' : ''}`} />
                </button>
              </div>
            </div>

            {/* Extensions List */}
            <div className="space-y-2.5">
              {filteredExtensions.map(pkg => (
                <div
                  key={pkg.packageName}
                  className="bg-[#121216] border border-[#27272A] rounded-2xl p-3.5 flex items-center justify-between gap-3"
                >
                  <div className="min-w-0 flex-1">
                    <div className="flex items-center gap-2">
                      <h4 className="text-sm font-bold text-white truncate">{pkg.name}</h4>
                      <span className="px-1.5 py-0.2 rounded bg-[#8B5CF6]/20 text-[#A78BFA] text-[9px] font-extrabold uppercase font-mono">
                        {pkg.lang}
                      </span>
                    </div>
                    <p className="text-xs text-zinc-400 truncate mt-0.5">
                      v{pkg.versionName} • {pkg.packageName}
                    </p>
                  </div>

                  <button
                    onClick={() => toggleExtensionInstall(pkg.packageName)}
                    className={`px-3.5 py-1.5 rounded-xl text-xs font-bold flex items-center gap-1.5 transition-all ${
                      pkg.installed
                        ? 'bg-rose-950/30 border border-rose-500/40 text-rose-400 hover:bg-rose-900/40'
                        : 'bg-[#8B5CF6] hover:bg-[#7C3AED] text-white shadow-md'
                    }`}
                  >
                    {pkg.installed ? (
                      <>
                        <Trash2 className="w-3.5 h-3.5" />
                        Uninstall
                      </>
                    ) : (
                      <>
                        <Download className="w-3.5 h-3.5" />
                        Install
                      </>
                    )}
                  </button>
                </div>
              ))}
            </div>
          </div>
        )}
      </main>
    </div>
  );
};
