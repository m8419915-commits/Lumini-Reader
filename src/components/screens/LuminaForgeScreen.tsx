import React, { useState } from 'react';
import {
  ArrowLeft,
  Sliders,
  Sparkles,
  Zap,
  Volume2,
  Check,
  Plus,
  Compass,
  Palette,
  Share2,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { ExperiencePack } from '../../types';

export const LuminaForgeScreen: React.FC = () => {
  const { experiencePacks, setActivePack, addExperiencePack, goBack, showToast } = useLumina();
  const [isCreatingPack, setIsCreatingPack] = useState(false);
  const [newTitle, setNewTitle] = useState('');
  const [newGenre, setNewGenre] = useState('Supernatural Action');
  const [newDirection, setNewDirection] = useState('Continuous Webtoon');
  const [newHaptics, setNewHaptics] = useState(0.8);
  const [newTheme, setNewTheme] = useState('AMOLED Pitch Black #000000');
  const [newAudio, setNewAudio] = useState('Action / Heavy Synth ılılı');
  const [newDescription, setNewDescription] = useState('');

  const handleCreate = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newTitle.trim()) return;

    const pack: ExperiencePack = {
      id: `forge_${Date.now()}`,
      title: newTitle.trim(),
      genre: newGenre,
      isActive: false,
      isDraft: false,
      readingDirection: newDirection,
      hapticIntensity: newHaptics,
      backgroundTheme: newTheme,
      audioProfile: newAudio,
      description: newDescription.trim() || 'Custom crafted experience pack with tuned haptics and ambient immersion.',
    };

    addExperiencePack(pack);
    setIsCreatingPack(false);
    setNewTitle('');
    setNewDescription('');
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
            <h2 className="text-sm font-black text-white flex items-center gap-2">
              Lumina Forge Studio
              <span className="text-[10px] bg-amber-500/20 text-amber-400 border border-amber-500/40 px-1.5 py-0.5 rounded font-mono">
                Packs
              </span>
            </h2>
            <p className="text-[11px] text-zinc-400">Experience packs, custom haptics & audio profiles</p>
          </div>
        </div>

        <button
          onClick={() => setIsCreatingPack(true)}
          className="px-3 py-1.5 rounded-xl bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-bold shadow-md flex items-center gap-1.5"
        >
          <Plus className="w-3.5 h-3.5" />
          Forge New
        </button>
      </header>

      <main className="max-w-xl mx-auto px-4 pt-4 space-y-5">
        {/* Active Pack Status Card */}
        <div className="p-4 rounded-2xl bg-gradient-to-br from-[#8B5CF6]/20 via-[#121216] to-[#00E5FF]/10 border border-[#8B5CF6]/40 space-y-2">
          <div className="flex items-center justify-between">
            <span className="text-[10px] font-extrabold uppercase tracking-wider text-[#A78BFA] flex items-center gap-1">
              <Sparkles className="w-3.5 h-3.5 text-[#00E5FF]" />
              Active Reading Engine Pack
            </span>
            <span className="text-xs font-mono text-emerald-400 font-bold">● SYNCED</span>
          </div>
          <h3 className="text-base font-black text-white">
            {experiencePacks.find(p => p.isActive)?.title || 'Default Lumina Setup'}
          </h3>
          <p className="text-xs text-zinc-300">
            {experiencePacks.find(p => p.isActive)?.description}
          </p>
        </div>

        {/* Experience Packs List */}
        <div className="space-y-3">
          <h3 className="text-xs font-extrabold uppercase tracking-wider text-zinc-400">
            Experience Library ({experiencePacks.length})
          </h3>

          <div className="space-y-3">
            {experiencePacks.map(pack => (
              <div
                key={pack.id}
                className={`bg-[#121216] border rounded-2xl p-4 space-y-3 transition-all ${
                  pack.isActive
                    ? 'border-[#8B5CF6] shadow-[0_0_16px_rgba(139,92,246,0.2)]'
                    : 'border-[#27272A] hover:border-[#27272A]/80'
                }`}
              >
                <div className="flex items-start justify-between">
                  <div>
                    <div className="flex items-center gap-2">
                      <h4 className="text-sm font-bold text-white">{pack.title}</h4>
                      {pack.isDraft && (
                        <span className="px-1.5 py-0.5 rounded bg-zinc-800 text-[9px] font-bold text-zinc-400">
                          Draft
                        </span>
                      )}
                    </div>
                    <p className="text-xs text-zinc-400 mt-0.5">{pack.genre}</p>
                  </div>

                  <button
                    onClick={() => setActivePack(pack.id)}
                    className={`px-3 py-1.5 rounded-xl text-xs font-bold flex items-center gap-1.5 transition-all ${
                      pack.isActive
                        ? 'bg-emerald-950/40 border border-emerald-500/40 text-emerald-400'
                        : 'bg-[#18181E] border border-[#27272A] text-zinc-300 hover:text-white'
                    }`}
                  >
                    {pack.isActive ? (
                      <>
                        <Check className="w-3.5 h-3.5" />
                        Active
                      </>
                    ) : (
                      'Apply Pack'
                    )}
                  </button>
                </div>

                <p className="text-xs text-zinc-300 leading-snug">{pack.description}</p>

                {/* Pack Attributes Pill Grid */}
                <div className="grid grid-cols-2 gap-2 pt-1 text-[11px] font-mono text-zinc-300">
                  <div className="bg-[#18181E] p-2 rounded-xl border border-[#27272A] flex items-center gap-2">
                    <Compass className="w-3.5 h-3.5 text-[#00E5FF]" />
                    <span className="truncate">{pack.readingDirection}</span>
                  </div>
                  <div className="bg-[#18181E] p-2 rounded-xl border border-[#27272A] flex items-center gap-2">
                    <Zap className="w-3.5 h-3.5 text-amber-400" />
                    <span>Haptics {Math.round(pack.hapticIntensity * 100)}%</span>
                  </div>
                  <div className="bg-[#18181E] p-2 rounded-xl border border-[#27272A] flex items-center gap-2">
                    <Palette className="w-3.5 h-3.5 text-[#A78BFA]" />
                    <span className="truncate">{pack.backgroundTheme}</span>
                  </div>
                  <div className="bg-[#18181E] p-2 rounded-xl border border-[#27272A] flex items-center gap-2">
                    <Volume2 className="w-3.5 h-3.5 text-emerald-400" />
                    <span className="truncate">{pack.audioProfile}</span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </main>

      {/* CREATE EXPERIENCE PACK MODAL */}
      {isCreatingPack && (
        <div className="fixed inset-0 z-50 bg-black/80 backdrop-blur-sm flex items-end sm:items-center justify-center p-0 sm:p-4">
          <form
            onSubmit={handleCreate}
            className="w-full max-w-lg bg-[#09090B] border border-[#27272A] rounded-t-2xl sm:rounded-2xl p-5 space-y-4 shadow-2xl max-h-[85vh] overflow-y-auto"
          >
            <div className="flex items-center justify-between pb-3 border-b border-[#27272A]">
              <h3 className="text-base font-bold text-white flex items-center gap-2">
                <Sliders className="w-4 h-4 text-[#8B5CF6]" />
                Forge New Experience Pack
              </h3>
              <button
                type="button"
                onClick={() => setIsCreatingPack(false)}
                className="text-xs text-zinc-400 hover:text-white"
              >
                Cancel
              </button>
            </div>

            <div className="space-y-1.5">
              <label className="text-xs font-bold text-zinc-300">Pack Title</label>
              <input
                type="text"
                required
                value={newTitle}
                onChange={e => setNewTitle(e.target.value)}
                placeholder="e.g., Midnight Horror Immersion"
                className="w-full bg-[#121216] border border-[#27272A] focus:border-[#8B5CF6] rounded-xl px-3 py-2 text-xs text-white outline-none"
              />
            </div>

            <div className="space-y-1.5">
              <label className="text-xs font-bold text-zinc-300">Target Genre / Mood</label>
              <input
                type="text"
                value={newGenre}
                onChange={e => setNewGenre(e.target.value)}
                placeholder="e.g., Cyberpunk Sci-Fi"
                className="w-full bg-[#121216] border border-[#27272A] focus:border-[#8B5CF6] rounded-xl px-3 py-2 text-xs text-white outline-none"
              />
            </div>

            <div className="space-y-1.5">
              <div className="flex items-center justify-between text-xs font-bold text-zinc-300">
                <span>Haptic Pulse Intensity</span>
                <span className="font-mono text-[#00E5FF]">{Math.round(newHaptics * 100)}%</span>
              </div>
              <input
                type="range"
                min="0"
                max="1"
                step="0.05"
                value={newHaptics}
                onChange={e => setNewHaptics(parseFloat(e.target.value))}
                className="w-full accent-[#8B5CF6] h-1.5 bg-[#27272A] rounded-lg cursor-pointer"
              />
            </div>

            <div className="space-y-1.5">
              <label className="text-xs font-bold text-zinc-300">Description</label>
              <textarea
                value={newDescription}
                onChange={e => setNewDescription(e.target.value)}
                rows={2}
                placeholder="Describe the environment vibe, ambient tuning, and reading style..."
                className="w-full bg-[#121216] border border-[#27272A] focus:border-[#8B5CF6] rounded-xl p-3 text-xs text-white outline-none"
              />
            </div>

            <button
              type="submit"
              className="w-full py-2.5 rounded-xl bg-[#8B5CF6] hover:bg-[#7C3AED] text-white text-xs font-bold shadow-lg"
            >
              Save Experience Pack
            </button>
          </form>
        </div>
      )}
    </div>
  );
};
