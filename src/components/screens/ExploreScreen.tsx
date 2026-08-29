import React, { useState } from 'react';
import {
  Globe,
  Puzzle,
  FolderArchive,
  Layers,
  ChevronRight,
  Download,
  Trash2,
  RefreshCw,
  Search,
  CheckCircle,
  Pin,
  PinOff,
  ShieldCheck,
  ShieldAlert,
  Flame,
  ArrowUpCircle,
  Shuffle,
  Eye,
  ExternalLink,
  BookOpen,
  Info,
  X,
  FileText,
  Clock
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { HeaderBar } from '../navigation/HeaderBar';
import { ExtensionPackage, SourceMeta } from '../../types';

export const ExploreScreen: React.FC = () => {
  const {
    extensions,
    sources,
    toggleExtensionInstall,
    installExtension,
    uninstallExtension,
    updateExtension,
    updateAllExtensions,
    toggleExtensionTrust,
    fetchExtensionsRepo,
    isFetchingRepo,
    togglePinSource,
    navigate,
  } = useLumina();

  const [activeTab, setActiveTab] = useState<'sources' | 'extensions' | 'migration'>('sources');
  const [selectedLang, setSelectedLang] = useState<string>('All');
  const [statusFilter, setStatusFilter] = useState<'all' | 'installed' | 'updates' | 'available' | 'untrusted'>('all');
  const [includeNsfw, setIncludeNsfw] = useState<boolean>(true);
  const [search, setSearch] = useState('');

  // Selected extension for detail modal
  const [inspectExtension, setInspectExtension] = useState<ExtensionPackage | null>(null);
  const [detailModalTab, setDetailModalTab] = useState<'readme' | 'changelog' | 'sources'>('readme');

  const languages = ['All', 'en', 'ja', 'ko', 'zh', 'es', 'fr', 'all'];

  // Extensions filter
  const filteredExtensions = extensions.filter(ext => {
    const matchesLang =
      selectedLang === 'All' ||
      ext.lang.toLowerCase() === selectedLang.toLowerCase() ||
      ext.lang === 'all';

    const matchesSearch =
      search.trim() === '' ||
      ext.name.toLowerCase().includes(search.toLowerCase()) ||
      ext.packageName.toLowerCase().includes(search.toLowerCase()) ||
      ext.repoName.toLowerCase().includes(search.toLowerCase());

    const matchesNsfw = includeNsfw || !ext.isNsfw;

    let matchesStatus = true;
    if (statusFilter === 'installed') matchesStatus = ext.status === 'installed' || ext.status === 'update_available';
    else if (statusFilter === 'updates') matchesStatus = ext.status === 'update_available';
    else if (statusFilter === 'available') matchesStatus = ext.status === 'available';
    else if (statusFilter === 'untrusted') matchesStatus = !ext.isTrusted || ext.status === 'untrusted';

    return matchesLang && matchesSearch && matchesNsfw && matchesStatus;
  });

  // Updates count
  const updatesCount = extensions.filter(e => e.status === 'update_available').length;

  // Sources grouping
  const pinnedSources = sources.filter(s => s.isPinned);
  const unpinnedSources = sources.filter(s => !s.isPinned);

  const filteredSources = (sourcesList: SourceMeta[]) =>
    sourcesList.filter(s => {
      const matchesLang = selectedLang === 'All' || s.lang === 'all' || s.lang.toLowerCase() === selectedLang.toLowerCase();
      const matchesSearch = search.trim() === '' || s.name.toLowerCase().includes(search.toLowerCase());
      const matchesNsfw = includeNsfw || !s.isNsfw;
      return matchesLang && matchesSearch && matchesNsfw;
    });

  return (
    <div className="min-h-screen bg-black text-white pb-24">
      <HeaderBar
        showRepoManager={true}
        onOpenRepoManager={() => navigate('repositories')}
      />

      <main className="max-w-2xl mx-auto px-4 pt-3 space-y-4">
        {/* Navigation Tabs (Sources vs Extensions vs Migration) */}
        <div className="p-1 rounded-2xl bg-[#121216] border border-[#27272A] flex">
          <button
            id="tab-sources-btn"
            onClick={() => setActiveTab('sources')}
            className={`flex-1 py-2 rounded-xl text-xs font-bold transition-all flex items-center justify-center gap-1.5 ${
              activeTab === 'sources'
                ? 'bg-[#8B5CF6] text-white shadow-[0_0_12px_rgba(139,92,246,0.4)]'
                : 'text-zinc-400 hover:text-white'
            }`}
          >
            <Globe className="w-3.5 h-3.5" />
            Sources ({sources.length})
          </button>

          <button
            id="tab-extensions-btn"
            onClick={() => setActiveTab('extensions')}
            className={`flex-1 py-2 rounded-xl text-xs font-bold transition-all flex items-center justify-center gap-1.5 ${
              activeTab === 'extensions'
                ? 'bg-[#8B5CF6] text-white shadow-[0_0_12px_rgba(139,92,246,0.4)]'
                : 'text-zinc-400 hover:text-white'
            }`}
          >
            <Puzzle className="w-3.5 h-3.5" />
            Extensions
            {updatesCount > 0 && (
              <span className="px-1.5 py-0.2 rounded-full bg-emerald-500 text-black text-[10px] font-black">
                {updatesCount}
              </span>
            )}
          </button>

          <button
            id="tab-migration-btn"
            onClick={() => navigate('migration')}
            className="flex-1 py-2 rounded-xl text-xs font-bold text-zinc-400 hover:text-white transition-all flex items-center justify-center gap-1.5"
          >
            <Shuffle className="w-3.5 h-3.5 text-[#00E5FF]" />
            Migrate
          </button>
        </div>

        {/* Global Search Bar */}
        <div className="relative">
          <Search className="absolute left-3.5 top-1/2 -translate-y-1/2 w-4 h-4 text-zinc-500" />
          <input
            id="explore-search-input"
            type="text"
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder={
              activeTab === 'sources'
                ? 'Search catalogues & sources...'
                : 'Search extensions, packages, repositories...'
            }
            className="w-full bg-[#121216] border border-[#27272A] focus:border-[#8B5CF6] rounded-xl pl-10 pr-4 py-2 text-xs text-white placeholder-zinc-500 outline-none"
          />
        </div>

        {/* ================= SOURCES TAB ================= */}
        {activeTab === 'sources' && (
          <div className="space-y-5">
            {/* Quick Repository Action Header */}
            <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-3.5 flex items-center justify-between">
              <div className="flex items-center gap-2.5">
                <Layers className="w-4 h-4 text-[#8B5CF6]" />
                <div>
                  <h4 className="text-xs font-bold text-white">Repository Index</h4>
                  <p className="text-[10px] text-zinc-400">Keiyoushi & community sources active</p>
                </div>
              </div>
              <button
                id="open-repo-manager-btn"
                onClick={() => navigate('repositories')}
                className="px-3 py-1 bg-black border border-[#27272A] hover:border-[#8B5CF6] text-zinc-300 hover:text-white rounded-lg text-xs font-semibold flex items-center gap-1"
              >
                Repositories <ChevronRight className="w-3 h-3" />
              </button>
            </div>

            {/* Pinned Sources Section */}
            {pinnedSources.length > 0 && (
              <div className="space-y-2">
                <div className="flex items-center justify-between">
                  <span className="text-[11px] font-extrabold uppercase tracking-wider text-[#00E5FF] flex items-center gap-1.5">
                    <Pin className="w-3.5 h-3.5" /> Pinned Sources ({filteredSources(pinnedSources).length})
                  </span>
                </div>

                <div className="space-y-2">
                  {filteredSources(pinnedSources).map(source => (
                    <div
                      key={source.id}
                      id={`source-${source.id}`}
                      className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-2xl p-3.5 flex items-center justify-between gap-3 transition-colors"
                    >
                      <div
                        onClick={() => navigate('detail', 1)}
                        className="flex items-center gap-3 min-w-0 flex-1 cursor-pointer"
                      >
                        <img
                          src={source.icon}
                          alt={source.name}
                          onError={e => {
                            // Fallback icon
                            (e.target as HTMLElement).style.display = 'none';
                          }}
                          className="w-9 h-9 rounded-xl object-contain bg-black border border-zinc-800 p-1 flex-shrink-0"
                        />
                        <div className="min-w-0">
                          <div className="flex items-center gap-2">
                            <h4 className="text-xs font-bold text-white truncate">{source.name}</h4>
                            <span className="px-1.5 py-0.2 rounded bg-zinc-800 text-zinc-300 text-[9px] font-mono uppercase font-bold">
                              {source.lang}
                            </span>
                            {source.isNsfw && (
                              <span className="px-1.5 py-0.2 rounded bg-rose-950/50 border border-rose-500/40 text-rose-400 text-[9px] font-bold">
                                18+
                              </span>
                            )}
                          </div>
                          <p className="text-[10px] text-zinc-400 truncate">
                            {source.itemCount.toLocaleString()} series • v{source.version}
                          </p>
                        </div>
                      </div>

                      <div className="flex items-center gap-1.5 flex-shrink-0">
                        <button
                          onClick={() => togglePinSource(source.id)}
                          className="p-1.5 text-[#00E5FF] hover:bg-zinc-800 rounded-lg"
                          title="Unpin source"
                        >
                          <Pin className="w-3.5 h-3.5 fill-[#00E5FF]" />
                        </button>
                        <button
                          onClick={() => navigate('detail', 1)}
                          className="px-2.5 py-1 bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 hover:bg-[#8B5CF6]/30 text-[#A78BFA] text-xs font-bold rounded-lg transition-colors"
                        >
                          Browse
                        </button>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            )}

            {/* All Installed Sources */}
            <div className="space-y-2">
              <span className="text-[11px] font-extrabold uppercase tracking-wider text-zinc-400">
                All Sources ({filteredSources(unpinnedSources).length})
              </span>

              <div className="space-y-2">
                {filteredSources(unpinnedSources).map(source => (
                  <div
                    key={source.id}
                    id={`source-${source.id}`}
                    className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-2xl p-3.5 flex items-center justify-between gap-3 transition-colors"
                  >
                    <div
                      onClick={() => navigate('detail', 1)}
                      className="flex items-center gap-3 min-w-0 flex-1 cursor-pointer"
                    >
                      <img
                        src={source.icon}
                        alt={source.name}
                        className="w-9 h-9 rounded-xl object-contain bg-black border border-zinc-800 p-1 flex-shrink-0"
                      />
                      <div className="min-w-0">
                        <div className="flex items-center gap-2">
                          <h4 className="text-xs font-bold text-white truncate">{source.name}</h4>
                          <span className="px-1.5 py-0.2 rounded bg-zinc-800 text-zinc-300 text-[9px] font-mono uppercase font-bold">
                            {source.lang}
                          </span>
                          {source.isNsfw && (
                            <span className="px-1.5 py-0.2 rounded bg-rose-950/50 border border-rose-500/40 text-rose-400 text-[9px] font-bold">
                              18+
                            </span>
                          )}
                        </div>
                        <p className="text-[10px] text-zinc-400 truncate">
                          {source.itemCount.toLocaleString()} series • v{source.version}
                        </p>
                      </div>
                    </div>

                    <div className="flex items-center gap-1.5 flex-shrink-0">
                      <button
                        onClick={() => togglePinSource(source.id)}
                        className="p-1.5 text-zinc-500 hover:text-zinc-300 hover:bg-zinc-800 rounded-lg"
                        title="Pin source to top"
                      >
                        <Pin className="w-3.5 h-3.5" />
                      </button>
                      <button
                        onClick={() => navigate('detail', 1)}
                        className="px-2.5 py-1 bg-[#8B5CF6]/20 border border-[#8B5CF6]/40 hover:bg-[#8B5CF6]/30 text-[#A78BFA] text-xs font-bold rounded-lg transition-colors"
                      >
                        Browse
                      </button>
                    </div>
                  </div>
                ))}
              </div>
            </div>
          </div>
        )}

        {/* ================= EXTENSIONS TAB ================= */}
        {activeTab === 'extensions' && (
          <div className="space-y-4">
            {/* Status Filter Chips & Update All Button */}
            <div className="flex items-center justify-between gap-2 flex-wrap">
              <div className="flex items-center gap-1.5 overflow-x-auto no-scrollbar">
                {[
                  { id: 'all', label: 'All' },
                  { id: 'installed', label: 'Installed' },
                  { id: 'updates', label: `Updates (${updatesCount})` },
                  { id: 'available', label: 'Available' },
                  { id: 'untrusted', label: 'Untrusted' },
                ].map(tab => (
                  <button
                    key={tab.id}
                    onClick={() => setStatusFilter(tab.id as any)}
                    className={`px-3 py-1 rounded-full text-xs font-bold transition-colors ${
                      statusFilter === tab.id
                        ? 'bg-[#8B5CF6] text-white shadow-sm'
                        : 'bg-[#121216] border border-[#27272A] text-zinc-400 hover:text-zinc-200'
                    }`}
                  >
                    {tab.label}
                  </button>
                ))}
              </div>

              {updatesCount > 0 && (
                <button
                  id="update-all-extensions-btn"
                  onClick={updateAllExtensions}
                  className="px-3 py-1 bg-emerald-500 hover:bg-emerald-400 text-black font-extrabold rounded-full text-xs flex items-center gap-1 shadow-md"
                >
                  <ArrowUpCircle className="w-3.5 h-3.5" />
                  Update All ({updatesCount})
                </button>
              )}
            </div>

            {/* Language Selector & NSFW Toggle */}
            <div className="flex items-center justify-between gap-2 pt-1 border-t border-[#27272A]">
              <div className="flex items-center gap-1.5 overflow-x-auto no-scrollbar">
                {languages.map(lang => (
                  <button
                    key={lang}
                    onClick={() => setSelectedLang(lang)}
                    className={`px-2.5 py-0.5 rounded-md text-[11px] font-bold uppercase transition-all ${
                      selectedLang === lang
                        ? 'bg-[#8B5CF6]/30 border border-[#A78BFA] text-white'
                        : 'bg-black/60 border border-[#27272A] text-zinc-400 hover:text-zinc-200'
                    }`}
                  >
                    {lang}
                  </button>
                ))}
              </div>

              <div className="flex items-center gap-2 flex-shrink-0">
                <button
                  onClick={() => setIncludeNsfw(!includeNsfw)}
                  className={`px-2 py-0.5 rounded-md text-[10px] font-bold border transition-colors ${
                    includeNsfw
                      ? 'bg-rose-950/30 border-rose-500/40 text-rose-400'
                      : 'bg-zinc-900 border-zinc-700 text-zinc-500'
                  }`}
                  title="Toggle 18+ NSFW Extensions"
                >
                  18+ NSFW: {includeNsfw ? 'ON' : 'OFF'}
                </button>

                <button
                  id="sync-extensions-icon-btn"
                  onClick={fetchExtensionsRepo}
                  disabled={isFetchingRepo}
                  className="p-1.5 rounded-lg bg-[#121216] border border-[#27272A] hover:border-[#8B5CF6] text-zinc-300 hover:text-white"
                  title="Sync Keiyoushi Repository Index"
                >
                  <RefreshCw className={`w-3.5 h-3.5 ${isFetchingRepo ? 'animate-spin text-[#8B5CF6]' : ''}`} />
                </button>
              </div>
            </div>

            {/* Extensions List */}
            <div className="space-y-2.5">
              {filteredExtensions.map(pkg => (
                <div
                  key={pkg.packageName}
                  id={`extension-item-${pkg.packageName}`}
                  className="bg-[#121216] border border-[#27272A] rounded-2xl p-3.5 flex items-center justify-between gap-3"
                >
                  <div
                    onClick={() => setInspectExtension(pkg)}
                    className="min-w-0 flex-1 cursor-pointer"
                  >
                    <div className="flex items-center gap-2 flex-wrap">
                      <h4 className="text-xs font-bold text-white truncate">{pkg.name}</h4>
                      <span className="px-1.5 py-0.2 rounded bg-[#8B5CF6]/20 text-[#A78BFA] text-[9px] font-extrabold uppercase font-mono">
                        {pkg.lang}
                      </span>
                      {pkg.isNsfw && (
                        <span className="px-1.5 py-0.2 rounded bg-rose-950/40 border border-rose-500/30 text-rose-400 text-[9px] font-bold">
                          18+
                        </span>
                      )}
                      {!pkg.isTrusted && (
                        <span className="px-1.5 py-0.2 rounded bg-amber-500/20 border border-amber-500/40 text-amber-300 text-[9px] font-bold flex items-center gap-0.5">
                          <ShieldAlert className="w-2.5 h-2.5" /> Untrusted
                        </span>
                      )}
                    </div>
                    <p className="text-[11px] text-zinc-400 truncate mt-0.5 font-mono">
                      v{pkg.versionName} • {pkg.repoName} • lib {pkg.libVersion}
                    </p>
                  </div>

                  {/* Actions (Update / Install / Uninstall / Details) */}
                  <div className="flex items-center gap-1.5">
                    <button
                      onClick={() => setInspectExtension(pkg)}
                      className="p-1.5 bg-black border border-[#27272A] hover:border-zinc-500 text-zinc-300 rounded-lg text-xs"
                      title="Inspect Readme & Details"
                    >
                      <Eye className="w-3.5 h-3.5" />
                    </button>

                    {pkg.status === 'update_available' ? (
                      <button
                        onClick={() => updateExtension(pkg.packageName)}
                        className="px-3 py-1.5 rounded-xl bg-emerald-500 hover:bg-emerald-400 text-black text-xs font-extrabold flex items-center gap-1 shadow-md"
                      >
                        <ArrowUpCircle className="w-3.5 h-3.5" /> Update
                      </button>
                    ) : pkg.installed ? (
                      <button
                        onClick={() => uninstallExtension(pkg.packageName)}
                        className="px-3 py-1.5 rounded-xl bg-rose-950/30 border border-rose-500/40 text-rose-400 hover:bg-rose-900/40 text-xs font-bold flex items-center gap-1"
                      >
                        <Trash2 className="w-3.5 h-3.5" /> Uninstall
                      </button>
                    ) : (
                      <button
                        onClick={() => installExtension(pkg.packageName)}
                        className="px-3 py-1.5 rounded-xl bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-bold flex items-center gap-1 shadow-md"
                      >
                        <Download className="w-3.5 h-3.5" /> Install
                      </button>
                    )}
                  </div>
                </div>
              ))}
            </div>
          </div>
        )}
      </main>

      {/* ================= EXTENSION INSPECT MODAL ================= */}
      {inspectExtension && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-center justify-center p-4">
          <div
            id="extension-detail-modal"
            className="bg-[#121216] border border-[#27272A] rounded-2xl w-full max-w-lg overflow-hidden flex flex-col max-h-[85vh] shadow-2xl"
          >
            {/* Modal Header */}
            <div className="p-4 border-b border-[#27272A] flex items-start justify-between gap-3">
              <div className="flex items-center gap-3">
                <img
                  src={inspectExtension.icon}
                  alt={inspectExtension.name}
                  className="w-10 h-10 rounded-xl bg-black border border-zinc-800 p-1 object-contain"
                />
                <div>
                  <h3 className="text-sm font-black text-white flex items-center gap-2">
                    {inspectExtension.name}
                    {inspectExtension.isTrusted ? (
                      <ShieldCheck className="w-4 h-4 text-emerald-400" />
                    ) : (
                      <ShieldAlert className="w-4 h-4 text-amber-400" />
                    )}
                  </h3>
                  <p className="text-[11px] text-zinc-400 font-mono">
                    {inspectExtension.packageName}
                  </p>
                </div>
              </div>

              <button
                onClick={() => setInspectExtension(null)}
                className="p-1.5 rounded-lg text-zinc-400 hover:text-white hover:bg-zinc-800"
              >
                <X className="w-4 h-4" />
              </button>
            </div>

            {/* Untrusted Warning Banner */}
            {!inspectExtension.isTrusted && (
              <div className="bg-amber-950/30 border-b border-amber-900/40 p-3 flex items-start gap-2.5">
                <ShieldAlert className="w-4 h-4 text-amber-400 flex-shrink-0 mt-0.5" />
                <div className="space-y-1 flex-1 text-xs">
                  <p className="font-bold text-amber-300">Untrusted Extension Origin</p>
                  <p className="text-zinc-400 text-[11px]">
                    This extension is from an unverified third-party repository. Only trust if you verify the source code.
                  </p>
                  <button
                    onClick={() => toggleExtensionTrust(inspectExtension.packageName)}
                    className="px-2.5 py-1 bg-amber-500 text-black font-extrabold rounded-md text-[10px] mt-1"
                  >
                    Trust Extension
                  </button>
                </div>
              </div>
            )}

            {/* Modal Tabs */}
            <div className="flex border-b border-[#27272A] bg-black/40 text-xs font-bold">
              <button
                onClick={() => setDetailModalTab('readme')}
                className={`flex-1 py-2.5 flex items-center justify-center gap-1.5 ${
                  detailModalTab === 'readme'
                    ? 'border-b-2 border-[#8B5CF6] text-white'
                    : 'text-zinc-400 hover:text-zinc-200'
                }`}
              >
                <FileText className="w-3.5 h-3.5" /> README
              </button>
              <button
                onClick={() => setDetailModalTab('changelog')}
                className={`flex-1 py-2.5 flex items-center justify-center gap-1.5 ${
                  detailModalTab === 'changelog'
                    ? 'border-b-2 border-[#8B5CF6] text-white'
                    : 'text-zinc-400 hover:text-zinc-200'
                }`}
              >
                <Clock className="w-3.5 h-3.5" /> Changelog
              </button>
              <button
                onClick={() => setDetailModalTab('sources')}
                className={`flex-1 py-2.5 flex items-center justify-center gap-1.5 ${
                  detailModalTab === 'sources'
                    ? 'border-b-2 border-[#8B5CF6] text-white'
                    : 'text-zinc-400 hover:text-zinc-200'
                }`}
              >
                <Globe className="w-3.5 h-3.5" /> Sources ({inspectExtension.sources.length})
              </button>
            </div>

            {/* Modal Body */}
            <div className="p-4 overflow-y-auto space-y-3 flex-1 text-xs text-zinc-300 font-sans leading-relaxed">
              {detailModalTab === 'readme' && (
                <div className="space-y-2">
                  <div className="p-3 bg-black/60 rounded-xl border border-[#27272A] space-y-1.5">
                    <p className="text-[11px] font-mono text-zinc-400">
                      <strong>Repository:</strong> {inspectExtension.repoName} ({inspectExtension.repoId})
                    </p>
                    <p className="text-[11px] font-mono text-zinc-400">
                      <strong>Lib Version:</strong> {inspectExtension.libVersion} • <strong>Target SDK:</strong> Android 14 / ProtoBuf
                    </p>
                  </div>
                  <div className="whitespace-pre-wrap font-sans text-xs text-zinc-300">
                    {inspectExtension.readmeContent || 'No README documentation provided for this extension.'}
                  </div>
                </div>
              )}

              {detailModalTab === 'changelog' && (
                <div className="space-y-2 whitespace-pre-wrap font-mono text-xs text-zinc-300">
                  {inspectExtension.changelogContent || 'No changelog entries found.'}
                </div>
              )}

              {detailModalTab === 'sources' && (
                <div className="space-y-2">
                  {inspectExtension.sources.map(s => (
                    <div
                      key={s.id}
                      className="p-3 bg-black/60 rounded-xl border border-[#27272A] flex items-center justify-between"
                    >
                      <div>
                        <h5 className="text-xs font-bold text-white">{s.name}</h5>
                        <p className="text-[10px] text-zinc-400 font-mono">{s.baseUrl}</p>
                      </div>
                      <span className="px-2 py-0.5 bg-emerald-500/10 text-emerald-400 rounded text-[10px] font-bold">
                        {s.status.toUpperCase()}
                      </span>
                    </div>
                  ))}
                </div>
              )}
            </div>

            {/* Modal Actions Footer */}
            <div className="p-3 bg-black/60 border-t border-[#27272A] flex items-center justify-between">
              <span className="text-[10px] text-zinc-500 font-mono">
                APK: {inspectExtension.apk}
              </span>

              <div className="flex items-center gap-2">
                {inspectExtension.installed ? (
                  <button
                    onClick={() => {
                      uninstallExtension(inspectExtension.packageName);
                      setInspectExtension(null);
                    }}
                    className="px-4 py-1.5 bg-rose-950/40 border border-rose-500/40 text-rose-400 rounded-xl font-bold text-xs"
                  >
                    Uninstall
                  </button>
                ) : (
                  <button
                    onClick={() => {
                      installExtension(inspectExtension.packageName);
                      setInspectExtension(null);
                    }}
                    className="px-4 py-1.5 bg-[#8B5CF6] hover:bg-[#7C3AED] text-white rounded-xl font-bold text-xs shadow-md"
                  >
                    Install Extension
                  </button>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

