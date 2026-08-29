import React from 'react';
import {
  Sparkles,
  BarChart3,
  Dna,
  Sliders,
  Compass,
  GitBranch,
  CloudSync,
  Settings,
  ChevronRight,
  ShieldCheck,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';
import { HeaderBar } from '../navigation/HeaderBar';

export const MoreScreen: React.FC = () => {
  const { navigate } = useLumina();

  return (
    <div className="min-h-screen bg-black text-white pb-24">
      <HeaderBar />

      <main className="max-w-xl mx-auto px-4 pt-3 space-y-6">
        <div>
          <h2 className="text-xl font-black text-white">Lumina Ecosystem</h2>
          <p className="text-xs text-zinc-400 mt-0.5">
            Sequential image engine, ambient immersion, and narrative intelligence.
          </p>
        </div>

        {/* SECTION 1: INTELLIGENCE & AI */}
        <div className="space-y-2.5">
          <span className="text-[11px] font-extrabold uppercase tracking-wider text-[#00E5FF]">
            Intelligence & Assistant
          </span>

          <div className="space-y-2">
            <div
              onClick={() => navigate('ai')}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3.5">
                <div className="w-10 h-10 rounded-xl bg-[#8B5CF6]/15 border border-[#8B5CF6]/40 flex items-center justify-center text-[#A78BFA]">
                  <Sparkles className="w-5 h-5" />
                </div>
                <div>
                  <h4 className="text-sm font-bold text-white">Lumina AI Assistant</h4>
                  <p className="text-xs text-zinc-400">Semantic manga recommendations, chapter breakdowns</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>

            <div
              onClick={() => navigate('dna')}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#00E5FF]/50 rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3.5">
                <div className="w-10 h-10 rounded-xl bg-[#00E5FF]/15 border border-[#00E5FF]/40 flex items-center justify-center text-[#00E5FF]">
                  <BarChart3 className="w-5 h-5" />
                </div>
                <div>
                  <h4 className="text-sm font-bold text-white">Reading Intelligence & DNA</h4>
                  <p className="text-xs text-zinc-400">Reading velocity, streaks, narrative genome profile</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>

            <div
              onClick={() => navigate('mutation')}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-emerald-500/50 rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3.5">
                <div className="w-10 h-10 rounded-xl bg-emerald-500/15 border border-emerald-500/40 flex items-center justify-center text-emerald-400">
                  <Dna className="w-5 h-5" />
                </div>
                <div>
                  <h4 className="text-sm font-bold text-white">Lumina Mutation Engine</h4>
                  <p className="text-xs text-zinc-400">Day 30 Evolution 60% • Adaptive learned behaviors</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>
          </div>
        </div>

        {/* SECTION 2: IMMERSION & STORY WORLDS */}
        <div className="space-y-2.5">
          <span className="text-[11px] font-extrabold uppercase tracking-wider text-amber-400">
            Immersion & Story Worlds
          </span>

          <div className="space-y-2">
            <div
              onClick={() => navigate('forge')}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#8B5CF6]/50 rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3.5">
                <div className="w-10 h-10 rounded-xl bg-[#8B5CF6]/15 border border-[#8B5CF6]/40 flex items-center justify-center text-[#A78BFA]">
                  <Sliders className="w-5 h-5" />
                </div>
                <div>
                  <h4 className="text-sm font-bold text-white">Lumina Forge</h4>
                  <p className="text-xs text-zinc-400">Experience Packs • Custom haptics, ambient shaders</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>

            <div
              onClick={() => navigate('journey')}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-[#00E5FF]/50 rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3.5">
                <div className="w-10 h-10 rounded-xl bg-[#00E5FF]/15 border border-[#00E5FF]/40 flex items-center justify-center text-[#00E5FF]">
                  <Compass className="w-5 h-5" />
                </div>
                <div>
                  <h4 className="text-sm font-bold text-white">Journey Map</h4>
                  <p className="text-xs text-zinc-400">Constellation history map & orbital timeline</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>

            <div
              onClick={() => navigate('timeline', 1)}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] hover:border-amber-500/50 rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3.5">
                <div className="w-10 h-10 rounded-xl bg-amber-500/15 border border-amber-500/40 flex items-center justify-center text-amber-400">
                  <GitBranch className="w-5 h-5" />
                </div>
                <div>
                  <h4 className="text-sm font-bold text-white">Timeline Mode (Interactive Guide)</h4>
                  <p className="text-xs text-zinc-400">Chronological vs Publication narrative trees</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>
          </div>
        </div>

        {/* SECTION 3: SYSTEM & PREFERENCES */}
        <div className="space-y-2.5">
          <span className="text-[11px] font-extrabold uppercase tracking-wider text-zinc-500">
            Data & Preferences
          </span>

          <div className="space-y-2">
            <div
              onClick={() => navigate('backup')}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3.5">
                <div className="w-10 h-10 rounded-xl bg-zinc-800 flex items-center justify-center text-zinc-300">
                  <CloudSync className="w-5 h-5" />
                </div>
                <div>
                  <h4 className="text-sm font-bold text-white">Backup & Cloud Sync</h4>
                  <p className="text-xs text-zinc-400">Google Drive sync, .lumina backup vault</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>

            <div
              onClick={() => navigate('settings')}
              className="bg-[#121216] hover:bg-[#18181E] border border-[#27272A] rounded-2xl p-4 flex items-center justify-between cursor-pointer transition-colors"
            >
              <div className="flex items-center gap-3.5">
                <div className="w-10 h-10 rounded-xl bg-zinc-800 flex items-center justify-center text-zinc-300">
                  <Settings className="w-5 h-5" />
                </div>
                <div>
                  <h4 className="text-sm font-bold text-white">Reader Settings</h4>
                  <p className="text-xs text-zinc-400">Reading direction, AMOLED tint, Lumina Flow</p>
                </div>
              </div>
              <ChevronRight className="w-4 h-4 text-zinc-500" />
            </div>
          </div>
        </div>

        {/* Version info badge */}
        <div className="text-center py-4 text-xs text-zinc-600 space-y-1">
          <p className="font-semibold text-zinc-500">Lumina Reader Ecosystem • v2.4.0</p>
          <p>Lumina Noir AMOLED Engine</p>
        </div>
      </main>
    </div>
  );
};
