import React from 'react';
import {
  ArrowLeft,
  Dna,
  Cpu,
  Lock,
  Unlock,
  RotateCcw,
  Zap,
  Activity,
  ShieldCheck,
} from 'lucide-react';
import { useLumina } from '../../context/LuminaContext';

export const LuminaMutationScreen: React.FC = () => {
  const { mutationBehaviors, toggleMutationLock, goBack, showToast } = useLumina();

  return (
    <div className="min-h-screen bg-black text-white pb-16">
      <header className="sticky top-0 z-30 bg-black/90 backdrop-blur-md border-b border-[#27272A] px-4 py-3 flex items-center justify-between">
        <div className="flex items-center gap-3">
          <button
            onClick={goBack}
            className="w-9 h-9 rounded-full bg-[#121216] border border-[#27272A] flex items-center justify-center text-white hover:border-emerald-500 transition-colors"
          >
            <ArrowLeft className="w-4 h-4" />
          </button>
          <div>
            <h2 className="text-sm font-black text-white flex items-center gap-2">
              Mutation Engine
              <span className="text-[10px] bg-emerald-500/20 text-emerald-400 border border-emerald-500/40 px-1.5 py-0.5 rounded font-mono">
                Active
              </span>
            </h2>
            <p className="text-[11px] text-zinc-400">Autonomous adaptive reading behavior</p>
          </div>
        </div>
      </header>

      <main className="max-w-xl mx-auto px-4 pt-4 space-y-6">
        {/* EVOLUTION STATUS HERO CARD */}
        <div className="p-5 rounded-2xl bg-gradient-to-br from-emerald-950/40 via-[#121216] to-black border border-emerald-500/40 space-y-4">
          <div className="flex items-center justify-between">
            <div className="flex items-center gap-2">
              <div className="w-9 h-9 rounded-xl bg-emerald-500/20 border border-emerald-500/40 flex items-center justify-center text-emerald-400">
                <Dna className="w-5 h-5" />
              </div>
              <div>
                <h3 className="text-sm font-black text-white">Day 30 Evolution</h3>
                <p className="text-[11px] text-emerald-400 font-mono">60% Neural Pattern Adaptation</p>
              </div>
            </div>
            <span className="px-2.5 py-1 rounded-full bg-emerald-500/20 text-emerald-300 text-xs font-mono font-bold">
              GENOME v3
            </span>
          </div>

          <div className="w-full bg-[#27272A] h-2.5 rounded-full overflow-hidden">
            <div
              className="h-full bg-gradient-to-r from-emerald-500 via-[#00E5FF] to-[#8B5CF6] rounded-full shadow-[0_0_12px_#10B981]"
              style={{ width: '60%' }}
            />
          </div>

          <p className="text-xs text-zinc-300 leading-relaxed">
            The Mutation Engine automatically monitors your reading speed (PPM), tap coordinates, and zoom triggers to organically adapt reader responsiveness without requiring manual configuration.
          </p>
        </div>

        {/* SYSTEM VITALS */}
        <div className="grid grid-cols-2 gap-3">
          <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-1">
            <div className="flex items-center gap-2 text-[#00E5FF]">
              <Cpu className="w-4 h-4" />
              <span className="text-[10px] font-bold uppercase text-zinc-400">Neural Pattern</span>
            </div>
            <div className="text-sm font-bold text-white">Continuous Flow Mode</div>
            <p className="text-[10px] text-zinc-500 font-mono">Resonance score: 0.94</p>
          </div>

          <div className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 space-y-1">
            <div className="flex items-center gap-2 text-emerald-400">
              <Activity className="w-4 h-4" />
              <span className="text-[10px] font-bold uppercase text-zinc-400">Adaptation Loop</span>
            </div>
            <div className="text-sm font-bold text-white">3 Learned Rules Active</div>
            <p className="text-[10px] text-zinc-500 font-mono">Zero friction telemetry</p>
          </div>
        </div>

        {/* LEARNED BEHAVIORS LIST */}
        <div className="space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="text-xs font-extrabold uppercase tracking-wider text-emerald-400">
              Learned Behaviors ({mutationBehaviors.length})
            </h3>
            <button
              onClick={() => showToast('All learned behaviors reset to factory baseline')}
              className="text-xs text-zinc-400 hover:text-white flex items-center gap-1"
            >
              <RotateCcw className="w-3 h-3" />
              Reset All
            </button>
          </div>

          <div className="space-y-3">
            {mutationBehaviors.map(b => (
              <div
                key={b.id}
                className="bg-[#121216] border border-[#27272A] rounded-2xl p-4 flex items-start justify-between gap-3"
              >
                <div className="space-y-1 min-w-0 flex-1">
                  <div className="flex items-center gap-2">
                    <h4 className="text-xs font-bold text-white">{b.title}</h4>
                    <span className="px-1.5 py-0.2 rounded bg-zinc-800 text-[9px] font-mono text-zinc-400">
                      {b.type}
                    </span>
                  </div>
                  <p className="text-xs text-zinc-300 leading-snug">{b.description}</p>
                </div>

                <button
                  onClick={() => {
                    toggleMutationLock(b.id);
                    showToast(b.isLocked ? `Unlocked ${b.title}` : `Locked ${b.title}`);
                  }}
                  className={`p-2 rounded-xl border transition-all flex-shrink-0 ${
                    b.isLocked
                      ? 'bg-emerald-950/30 border-emerald-500/40 text-emerald-400'
                      : 'bg-zinc-900 border-zinc-700 text-zinc-400'
                  }`}
                  title={b.isLocked ? 'Locked (Behavior Preserved)' : 'Unlocked'}
                >
                  {b.isLocked ? <Lock className="w-4 h-4" /> : <Unlock className="w-4 h-4" />}
                </button>
              </div>
            ))}
          </div>
        </div>
      </main>
    </div>
  );
};
